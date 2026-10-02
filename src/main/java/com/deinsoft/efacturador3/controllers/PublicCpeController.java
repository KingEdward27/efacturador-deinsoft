package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint público para consulta y descarga de comprobantes electrónicos.
 * No requiere autenticación. Solo expone comprobantes con respuesta de SUNAT
 * (indSituacion 03 = ACEPTADO, 04 = ACEPTADO CON OBSERVACIONES).
 */
@RestController
@RequestMapping("api/v1/public/cpe")
public class PublicCpeController {

    private static final Logger log = LoggerFactory.getLogger(PublicCpeController.class);

    /** Solo comprobantes que ya tienen respuesta definitiva de SUNAT */
    private static final Set<String> SITUACIONES_VISIBLES = new HashSet<>(
            Arrays.asList("03", "04")
    );

    private static final Map<String, String> TIPO_NOMBRES = new HashMap<>();
    private static final Map<String, String> ESTADO_NOMBRES = new HashMap<>();

    static {
        TIPO_NOMBRES.put("01", "Factura Electrónica");
        TIPO_NOMBRES.put("03", "Boleta de Venta");
        TIPO_NOMBRES.put("07", "Nota de Crédito");
        TIPO_NOMBRES.put("08", "Nota de Débito");

        ESTADO_NOMBRES.put("03", "ACEPTADO");
        ESTADO_NOMBRES.put("04", "ACEPTADO CON OBSERVACIONES");
        ESTADO_NOMBRES.put("10", "RECHAZADO");
    }

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private FacturaElectronicaService facturaElectronicaService;

    // ── Consulta metadata ────────────────────────────────────────

    @GetMapping
    public ResponseEntity<?> consultar(
            @RequestParam String ruc_emisor,
            @RequestParam(required = false) String tipo,
            @RequestParam String serie,
            @RequestParam String numero,
            @RequestParam(required = false) Double monto_total) {

        FacturaElectronica fe = buscarCpe(ruc_emisor, tipo, serie, numero);
        if (fe == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("ok", false);
            err.put("message", "Comprobante no encontrado o aún en proceso de envío a SUNAT");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }

        if (monto_total != null && fe.getImporteTotal() != null) {
            double diff = Math.abs(fe.getImporteTotal().doubleValue() - monto_total);
            if (diff > 0.01) {
                log.warn("CPE monto mismatch {}-{}: esperado={} recibido={}", serie, numero, fe.getImporteTotal(), monto_total);
                Map<String, Object> err = new HashMap<>();
                err.put("ok", false);
                err.put("message", "Comprobante no encontrado o aún en proceso de envío a SUNAT");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
            }
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("ok", true);
        resp.put("data", buildData(fe));
        return ResponseEntity.ok(resp);
    }

    // ── Descarga PDF ─────────────────────────────────────────────

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> descargarPdf(
            @RequestParam String ruc_emisor,
            @RequestParam(required = false) String tipo,
            @RequestParam String serie,
            @RequestParam String numero) {
        try {
            FacturaElectronica fe = buscarCpe(ruc_emisor, tipo, serie, numero);
            if (fe == null) return ResponseEntity.notFound().build();

            byte[] pdf = facturaElectronicaService.getPDFInBtyes(fe.getId(), 1); // 1 = A4
            if (pdf == null || pdf.length == 0) return ResponseEntity.notFound().build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + buildFilename(fe, "pdf") + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            log.error("Error al generar PDF para {}-{}: {}", serie, numero, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ── Descarga XML ─────────────────────────────────────────────

    @GetMapping("/xml")
    public ResponseEntity<byte[]> descargarXml(
            @RequestParam String ruc_emisor,
            @RequestParam(required = false) String tipo,
            @RequestParam String serie,
            @RequestParam String numero) {
        try {
            FacturaElectronica fe = buscarCpe(ruc_emisor, tipo, serie, numero);
            if (fe == null) return ResponseEntity.notFound().build();

            Map<String, Object> xmlMap = facturaElectronicaService.getXmlAsBytes(
                    fe.getId(), fe.getSerie(), fe.getNumero(), fe.getEmpresa());
            if (xmlMap == null) return ResponseEntity.notFound().build();

            String base64  = (String) xmlMap.get("xmlBase64");
            String fileName = (String) xmlMap.getOrDefault("fileName", buildFilename(fe, "xml"));
            byte[] xmlBytes = Base64.getDecoder().decode(base64);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.parseMediaType("application/xml; charset=UTF-8"))
                    .body(xmlBytes);
        } catch (Exception e) {
            log.error("Error al obtener XML para {}-{}: {}", serie, numero, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ── helpers ──────────────────────────────────────────────────

    private FacturaElectronica buscarCpe(String rucEmisor, String tipo, String serie, String numero) {
        Empresa empresa = empresaService.findByNumdoc(rucEmisor);
        if (empresa == null) return null;

        FacturaElectronica param = new FacturaElectronica();
        param.setSerie(serie.toUpperCase());
        param.setNumero(numero);
        param.setEmpresa(empresa);

        List<FacturaElectronica> list = facturaElectronicaService.getBySerieAndNumeroAndEmpresaId(param);
        if (list == null || list.isEmpty()) return null;

        return list.stream()
                .filter(f -> "1".equals(f.getEstado()))
                .filter(f -> SITUACIONES_VISIBLES.contains(f.getIndSituacion()))
                .filter(f -> tipo == null || tipo.equals(f.getTipo()))
                .findFirst()
                .orElse(null);
    }

    private Map<String, Object> buildData(FacturaElectronica fe) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("tipo_nombre",       TIPO_NOMBRES.getOrDefault(fe.getTipo(), "Comprobante"));
        d.put("serie",             fe.getSerie());
        d.put("numero",            fe.getNumero());
        d.put("fecha_emision",     fe.getFechaEmision() != null ? fe.getFechaEmision().toString() : null);
        d.put("emisor",            fe.getEmpresa().getRazonSocial());
        d.put("emisor_ruc",        fe.getEmpresa().getNumdoc());
        d.put("receptor",          fe.getClienteNombre());
        d.put("receptor_doc",      fe.getClienteDocumento());
        d.put("receptor_doc_tipo", fe.getClienteTipo());  // "6"=RUC, "1"=DNI
        d.put("moneda",            "PEN");
        d.put("monto_total",       fe.getImporteTotal());
        d.put("igv",               fe.getSumatoriaIGV());
        d.put("subtotal",          fe.getTotalValorVenta());
        d.put("estado_sunat",      ESTADO_NOMBRES.getOrDefault(fe.getIndSituacion(), fe.getIndSituacion()));
        d.put("observacion",       fe.getObservacionEnvio());
        return d;
    }

    private String buildFilename(FacturaElectronica fe, String ext) {
        String num;
        try {
            num = String.format("%08d", Integer.parseInt(fe.getNumero()));
        } catch (NumberFormatException e) {
            num = fe.getNumero();
        }
        return fe.getEmpresa().getNumdoc() + "-" + fe.getTipo() + "-"
                + fe.getSerie() + "-" + num + "." + ext;
    }
}

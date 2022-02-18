/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ComprobanteDet;
import com.deinsoft.efacturador3.bean.FacturaElectronica;
import com.deinsoft.efacturador3.bean.FacturaElectronicaDet;
import com.deinsoft.efacturador3.commons.controllers.CommonController;
import com.deinsoft.efacturador3.commons.service.CommonService;
import com.deinsoft.efacturador3.commons.service.CommonServiceImpl;
import com.deinsoft.efacturador3.model.Documento;
import com.deinsoft.efacturador3.service.BandejaDocumentosService;
import com.deinsoft.efacturador3.service.ComunesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.deinsoft.efacturador3.repository.FacturaElectronicaRepository;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;

/**
 *
 * @author EDWARD-PC
 */
@RestController
public class GenerateDocumentController {

    private static final Logger log = LoggerFactory.getLogger(GenerateDocumentController.class);

    @Autowired
    private BandejaDocumentosService bandejaDocumentosService;

    @Autowired
    private ComunesService comunesService;

    @Autowired
    FacturaElectronicaRepository facturaElectronicaRepository;
//    @Autowired
//    CommonService<FacturaElectronica> commonService;

    @PostMapping(value = "/send-document")
    public ResponseEntity<?> sendDocument(@Valid @RequestBody ComprobanteCab documento, BindingResult result) throws TransferirArchivoException, ParseException {
        if (result.hasErrors()) {
            return this.validar(result);
        }
        BigDecimal totalValorVentasGravadas = BigDecimal.ZERO, totalValorVentasInafectas = BigDecimal.ZERO,
                totalValorVentasExoneradas = BigDecimal.ZERO,
                SumatoriaIGV = BigDecimal.ZERO, SumatoriaISC = BigDecimal.ZERO,
                sumatoriaOtrosTributos = BigDecimal.ZERO, sumatoriaOtrosCargos = BigDecimal.ZERO, totalValorVenta = BigDecimal.ZERO;
        for (ComprobanteDet detalle : documento.getLista()) {
            if (Integer.valueOf(detalle.getTipo_igv()) >= 10 && Integer.valueOf(detalle.getTipo_igv()) <= 20) {
                totalValorVentasGravadas = totalValorVentasGravadas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            } else if (Integer.valueOf(detalle.getTipo_igv()) >= 30 && Integer.valueOf(detalle.getTipo_igv()) <= 36) {
                totalValorVentasInafectas = totalValorVentasInafectas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            } else {
                totalValorVentasExoneradas = totalValorVentasExoneradas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            }
            SumatoriaIGV = SumatoriaIGV.add(detalle.getAfectacionIGV());
            SumatoriaISC = SumatoriaISC.add(detalle.getAfectacionISC() == null ? BigDecimal.ZERO : detalle.getAfectacionISC());
            totalValorVenta = totalValorVenta.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
        }
        documento.setSumatoriaIGV(SumatoriaIGV);
        documento.setSumatoriaISC(SumatoriaISC);
        documento.setSumatoriaOtrosCargos(BigDecimal.ZERO);
        documento.setSumatoriaOtrosTributos(BigDecimal.ZERO);
        documento.setTotalValorVentasGravadas(totalValorVentasGravadas);
        documento.setTotalValorVentasInafectas(totalValorVentasInafectas);
        documento.setTotalValorVentasExoneradas(totalValorVentasExoneradas);

        FacturaElectronica comprobante = new FacturaElectronica();
        comprobante.setTipo(documento.getTipo());
        comprobante.setTipoOperacion(documento.getTipo_operacion());
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(documento.getFecha_emision());
//	java.sql.Date dateSql = new java.sql.Date(date1.getTime());
        comprobante.setFechaEmision(date1);
        comprobante.setSerie(documento.getSerie());
        comprobante.setNumero(documento.getNumero());
        comprobante.setFechaVencimiento(documento.getFecha_vencimiento());
        comprobante.setClienteTipo(documento.getCliente_tipo());
        comprobante.setClienteNombre(documento.getCliente_nombre());
        comprobante.setClienteDocumento(documento.getCliente_documento());
        comprobante.setClienteDireccion(documento.getCliente_direccion());
        comprobante.setClienteEmail(documento.getCliente_email());
        comprobante.setClienteTelefono(documento.getCliente_telefono());
        comprobante.setVendedorNombre(documento.getVendedor_nombre());
        comprobante.setObservaciones(documento.getObservaciones());
        comprobante.setPlacaVehiculo(documento.getPlaca_vehiculo());
        comprobante.setOrdenCompra(documento.getOrden_compra());
        comprobante.setGuiaRemision(documento.getGuia_remision());
        comprobante.setDescuentoGlobalPorcentaje(documento.getDescuento_global_porcentaje());
        comprobante.setMoneda(documento.getMoneda());
        comprobante.setNotaTipo(documento.getNota_tipo());
        comprobante.setNotaMotivo(documento.getNota_motivo());
        comprobante.setNotaReferenciaTipo(documento.getNota_referencia_tipo());
        comprobante.setNotaReferenciaSerie(documento.getNota_referencia_serie());
        comprobante.setNotaReferenciaNumero(documento.getNota_referencia_numero());
        comprobante.setIncluirPdf(documento.getIncluir_pdf());
        comprobante.setIncluirXml(documento.getIncluir_xml());
        comprobante.setSumatoriaIGV(SumatoriaIGV);
        comprobante.setSumatoriaISC(SumatoriaISC);
        comprobante.setSumatoriaOtrosCargos(BigDecimal.ZERO);
        comprobante.setSumatoriaOtrosTributos(BigDecimal.ZERO);
        comprobante.setTotalValorVentasGravadas(totalValorVentasGravadas);
        comprobante.setTotalValorVentasInafectas(totalValorVentasInafectas);
        comprobante.setTotalValorVentasExoneradas(totalValorVentasExoneradas);
        comprobante.setTotalValorVenta(totalValorVenta);
        List<FacturaElectronicaDet> list = new ArrayList<>();
        for (ComprobanteDet comprobanteDet : documento.getLista()) {
            FacturaElectronicaDet det = new FacturaElectronicaDet();
            det.setDescripcion(comprobanteDet.getDescripcion());
            det.setUnidadMedida(comprobanteDet.getUnidad_medida());
            det.setCantidad(comprobanteDet.getCantidad());
            det.setPrecioVentaUnitario(comprobanteDet.getPrecio_unitario());
            det.setValorVentaItem(comprobanteDet.getPrecio_unitario());
            det.setValorUnitario(comprobanteDet.getPrecio_unitario());
            det.setAfectacionIgv(comprobanteDet.getAfectacionIGV());
            det.setAfectacionIGVCode(comprobanteDet.getTipo_igv());
            det.setDescuento(comprobanteDet.getDescuento_porcentaje().
                    divide(new BigDecimal(100)).
                    multiply(comprobanteDet.getCantidad().multiply(comprobanteDet.getPrecio_unitario())));
            det.setRecargo(comprobanteDet.getRecargo());
            list.add(det);

        }
        comprobante.setListFacturaElectronicaDet(list);
        comprobante.getListFacturaElectronicaDet().stream().forEach(item -> {
            comprobante.addFacturaElectronicaDet(item);
        });
        comprobante.setIndSituacion("01");
        FacturaElectronica facturaElectronicaResult = facturaElectronicaRepository.save(comprobante);

        String docGene = this.bandejaDocumentosService.generarComprobantePagoSunat(facturaElectronicaResult);
        log.info("docGene: " + docGene);
        if ("".equals(docGene)) {
            facturaElectronicaResult.setFechaGenXml(new Date());
            facturaElectronicaResult.setIndSituacion("02");
            facturaElectronicaResult = facturaElectronicaRepository.save(facturaElectronicaResult);
//            listadoBandeja = this.bandejaDocumentosService.consultarBandejaComprobantes();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaElectronicaResult);
    }

    protected ResponseEntity<?> validar(BindingResult result) {
        Map<String, Object> errores = new HashMap<>();
//		final List<String> errors = result.getAllErrors().stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), " El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errores);
    }
//    @PostMapping(value = "/generar-xml")
//    public ResponseEntity<?> generarXml(@RequestBody Documento documento) {
//        log.debug("iniciarProceso...Iniciando el procesamiento");
//        HashMap<String, Object> resultado = new HashMap<>();
//        String mensajeError = "", retorno = "";
//        Documento documentoResp = null;
//        List<Documento> listadoBandeja = null;
////        URI uri = UriHelper.getUriForId(uriInfo, Integer.valueOf(12));
//        try {
//            String mensajeValidacion = this.bandejaDocumentosService.validarParametroRegistrado();
//
//            Boolean versionActualizada = this.comunesService.validarVersionFacturador("1.3.2");
//            log.debug("versionActualizada: " + versionActualizada);
//
//            if ("".equals(mensajeValidacion) && versionActualizada.booleanValue()) {
//                List<Documento> lista = this.bandejaDocumentosService.consultarBandejaComprobantesPorId(documento);
//                log.debug("lista: " + lista);
//
//                if (lista.size() > 0) {
//                    documentoResp = lista.get(0);
//                    log.debug("documentoResp: " + documentoResp);
//                    String docGene = this.bandejaDocumentosService.generarComprobantePagoSunat(documentoResp);
//                    log.debug("docGene: " + docGene);
//                    if ("".equals(docGene)) {
//                        documentoResp.setFecGene("FECHA_GENERACION");
//                        documentoResp.setIndSitu("02");
//                        documentoResp.setDesObse("-");
//                        this.bandejaDocumentosService.actualizarEstadoBandejaCdp(documentoResp);
//                        listadoBandeja = this.bandejaDocumentosService.consultarBandejaComprobantes();
//                    }
//                } else {
//                    listadoBandeja = new ArrayList<>();
//                }
//            } else {
//                listadoBandeja = new ArrayList<>();
//
//                mensajeError = mensajeValidacion;
//
//                if (!versionActualizada.booleanValue()) {
//                    mensajeError = "No existen datos que procesar.";
//                }
//            }
//
//            resultado = new HashMap<>();
//            resultado.put("listaBandejaFacturador", listadoBandeja);
//            resultado.put("mensaje", mensajeError);
//            resultado.put("validacion", "EXITO");
//        } catch (Exception e) {
//            try {
//                log.error("Error facturador", e.getMessage(), e);
//                documentoResp.setIndSitu("06");
//                documentoResp.setDesObse(e.getMessage());
//                this.bandejaDocumentosService.actualizarEstadoBandejaCdp(documentoResp);
//                listadoBandeja = this.bandejaDocumentosService.consultarBandejaComprobantes();
//                resultado.put("listaBandejaFacturador", listadoBandeja);
//                resultado.put("mensaje", e.getMessage());
//                resultado.put("validacion", "EXITO");
//            } catch (Exception ex) {
//
//                log.error("Error facturador " + ex.getMessage(), e);
//                resultado.put("mensaje", ex.getMessage());
//                resultado.put("validacion", "FALLO");
//            }
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            retorno = mapper.writeValueAsString(resultado);
//        } catch (Exception e) {
//            mensajeError = "Error: " + e.getMessage() + " Causa: " + e.getCause();
//            log.error(mensajeError);
//            resultado.put("mensaje", mensajeError);
//            resultado.put("validacion", "FALLO");
//            log.error(e.getMessage(), e);
//        }
//
//        log.debug("SoftwareFacturadorController.generarXml...Terminando el procesamiento");
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
//    }
}

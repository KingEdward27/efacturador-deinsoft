package com.deinsoft.efacturador3.parser.pipe;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ComprobanteDet;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaCuotas;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.FacturaElectronicaLeyenda;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.parser.Parser;
import com.deinsoft.efacturador3.parser.ParserException;
import com.deinsoft.efacturador3.util.Impresion;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipeFacturaParser
        extends PipeCpeAbstractParser
        implements Parser {

    private static final Logger log = LoggerFactory.getLogger(PipeFacturaParser.class);

    private static String plantillaSeleccionada = "ConvertirFacturaXML.ftl";

    private FacturaElectronica cabecera;

    private String nombreArchivo;

    private Empresa contri;

    private String archivoRelacionado = "";

    private String archivoAdiCabecera = "";

    private String archivoAdiDetalle = "";

    private String archivoLeyendas = "";

    private String archivoAdiTributos = "";

    private String archivoAdiVariableGlobal = "";

    /*     */
    public PipeFacturaParser(Empresa contri, FacturaElectronica cabecera
    //            ,
    //             String[] archivos,
    //            String nombreArchivo
    ) {
        this.contri = contri;
        this.cabecera = cabecera;
//        this.detalle = detalle;
//
//        this.nombreArchivo = nombreArchivo;
//
//        this.archivoCabecera = archivos[0];
//        this.archivoDetalle = archivos[1];
//        this.archivoRelacionado = archivos[2];
//        this.archivoAdiCabecera = archivos[3];
//        this.archivoAdiDetalle = archivos[4];
//        this.archivoLeyendas = archivos[5];
//        this.archivoAdiTributos = archivos[6];
//        this.archivoAdiVariableGlobal = archivos[7];
    }

    public Map<String, Object> pipeToMap() throws ParserException {
        log.debug("SoftwareFacturadorController.formatoResumenBajas...Inicio Procesamiento");

//        String[] datosArchivo = this.nombreArchivo.split("\\-");
        String identificadorFirmaSwf = "SIGN";
        Random calcularRnd = new Random();
        Integer codigoFacturadorSwf = Integer.valueOf((int) (calcularRnd.nextDouble() * 1000000.0D));

//        log.debug("SoftwareFacturadorController.formatoResumenBajas...Leyendo Archivo: " + this.archivoCabecera);
        Map<String, Object> factura = new HashMap<>();
//
//        Path fileCabecera = Paths.get(this.archivoCabecera, new String[0]);
//
//        if (!Files.exists(fileCabecera, new java.nio.file.LinkOption[0])) {
//            throw new ParserException("El archivo no existe: " + this.archivoCabecera);
//        }
//
//        try (InputStream in = Files.newInputStream(fileCabecera, new java.nio.file.OpenOption[0]);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
//            String cadena = null;
//
//            while ((cadena = reader.readLine()) != null) {
//                String[] registro = cadena.split("\\|");
//
//                if (registro.length != 18) {
//                    throw new ParserException("El archivo cabecera no continene la cantidad de datos esperada (18 columnas).");
//                }
//                Date d = sdf.parse(oldDateString);
//                sdf.applyPattern(NEW_FORMAT);
//                Date d = sdf.parse(oldDateString);
//                sdf.applyPattern(NEW_FORMAT);
        String sumTotalAnticipos = cabecera.getDocrefMonto() != null ? Impresion.df.format(cabecera.getDocrefMonto()) : "0.00";
        factura = new HashMap<>();
        factura.put("tipOperacion", cabecera.getTipoOperacion());
        factura.put("fecEmision", cabecera.getFechaEmision().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        factura.put("horEmision", "12:00:00");
        factura.put("fecVencimiento", cabecera.getFechaEmision().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        factura.put("codLocalEmisor", cabecera.getCodLocal());
        factura.put("tipDocUsuario", cabecera.getClienteTipo());
        factura.put("numDocUsuario", cabecera.getClienteDocumento());
        factura.put("rznSocialUsuario", cabecera.getClienteNombre());
        factura.put("moneda", cabecera.getMoneda());

        factura.put("sumTotTributos", Impresion.df.format(cabecera.getSumatoriaIGV().add(cabecera.getSumatoriaISC())));
        factura.put("sumTotValVenta", Impresion.df.format(cabecera.getTotalValorVentasGravadas().subtract(cabecera.getSumatoriaIGV())));
        factura.put("sumPrecioVenta", Impresion.df.format(cabecera.getTotalValorVentasGravadas()));
        factura.put("sumDescTotal", cabecera.getDescuentosGlobales() == null ? "0.00" : Impresion.df.format(cabecera.getDescuentosGlobales()));
        factura.put("sumOtrosCargos", cabecera.getSumatoriaOtrosCargos() == null ? "0.00" : Impresion.df.format(cabecera.getSumatoriaOtrosCargos()));
        factura.put("sumTotalAnticipos", sumTotalAnticipos);
        factura.put("sumImpVenta", Impresion.df.format(cabecera.getTotalValorVenta()));

        factura.put("ublVersionId", "2.1");
        factura.put("customizationId", cabecera.getCustomizationId());

        factura.put("ublVersionIdSwf", "2.0");
        factura.put("CustomizationIdSwf", "1.0");
        factura.put("nroCdpSwf", cabecera.getSerie() + "-" + String.format("%08d", Integer.parseInt(cabecera.getNumero())));
        factura.put("tipCdpSwf", cabecera.getTipo());
        factura.put("nroRucEmisorSwf", this.contri.getNumdoc());
        factura.put("tipDocuEmisorSwf", cabecera.getEmpresa().getTipodoc().toString());
        factura.put("nombreComercialSwf", this.contri.getRazonSocial());
        factura.put("razonSocialSwf", this.contri.getRazonSocial());
        factura.put("ubigeoDomFiscalSwf", "");
        factura.put("direccionDomFiscalSwf", "");
        factura.put("deparSwf", "");
        factura.put("provinSwf", "");
        factura.put("distrSwf", "");
        factura.put("urbanizaSwf", "");
        factura.put("paisDomFiscalSwf", "PE");
        factura.put("codigoMontoDescuentosSwf", "2005");
        factura.put("codigoMontoOperGravadasSwf", "1001");
        factura.put("codigoMontoOperInafectasSwf", "1002");
        factura.put("codigoMontoOperExoneradasSwf", "1003");
        factura.put("idIgv", "1000");
        factura.put("codIgv", "IGV");
        factura.put("codExtIgv", "VAT");
        factura.put("idIsc", "2000");
        factura.put("codIsc", "ISC");
        factura.put("codExtIsc", "EXC");
        factura.put("idOtr", "9999");
        factura.put("codOtr", "OTROS");
        factura.put("codExtOtr", "OTH");
        factura.put("tipoCodigoMonedaSwf", "01");
        factura.put("identificadorFacturadorSwf", "SISTEMA FACTURADOR DEFACTURADOR - DEINSOFT SRL");
        factura.put("codigoFacturadorSwf", codigoFacturadorSwf.toString());
        factura.put("identificadorFirmaSwf", identificadorFirmaSwf);
        factura.put("formaPago", cabecera.getFormaPago());
        factura.put("tipMonedaMtoNetoPendientePago", cabecera.getTipoMonedaMontoNetoPendiente());
//                factura.put("mtoNetoPendientePago", String.format("%.2f",cabecera.getMontoNetoPendiente()).replace(",", "."));
        factura.put("mtoNetoPendientePago", String.format("%.2f", cabecera.getMontoNetoPendiente()).replace(",", "."));

        factura.put("ctaBancoNacionDetraccion", cabecera.getCtaBancoNacionDetraccion());
        factura.put("codBienDetraccion", cabecera.getCodBienDetraccion());
        factura.put("porDetraccion", cabecera.getPorDetraccion());
        factura.put("mtoDetraccion", cabecera.getMtoDetraccion());
        factura.put("codMedioPago", "001");
        
        log.debug("cabecera factura plano:" + factura);

//            }
//
//        } catch (IOException x) {
//            throw new ParserException("No se pudo leer el archivo cabecera: " + this.archivoCabecera, x);
//        }
//        Path fileDetalle = Paths.get(this.archivoDetalle, new String[0]);
//
//        if (!Files.exists(fileDetalle, new java.nio.file.LinkOption[0])) {
//            throw new ParserException("El archivo no existe: " + this.archivoDetalle);
//        }
        List<Map<String, Object>> listaDetalle = new ArrayList<>();
        Map<String, Object> detalle = null;

//        try (InputStream in = Files.newInputStream(fileDetalle, new java.nio.file.OpenOption[0]);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
//            String cadena = null;
//
//            Integer linea = Integer.valueOf(0);
//
//            while ((cadena = reader.readLine()) != null) {
//                String[] registro = cadena.split("\\|");
//                if (registro.length != 36) {
//                    throw new ParserException("El archivo detalle no continene la cantidad de datos esperada (36 columnas).");
//                }
        int contadorItem = 0;
        for (FacturaElectronicaDet item : cabecera.getListFacturaElectronicaDet()) {
//            linea = Integer.valueOf(linea.intValue() + 1);
//            if (item.getPrecioVentaUnitario().compareTo(BigDecimal.ZERO) == 0) {
//                continue;
//            }
            detalle = new HashMap<>();
            contadorItem++;
            detalle.put("codProducto", item.getCodigo());
            detalle.put("desItem", item.getDescripcion());
            detalle.put("mtoValorUnitario", item.getValorUnitario());

            detalle.put("sumTotTributosItem", String.valueOf(item.getAfectacionIgv()));
            detalle.put("unidadMedida", item.getUnidadMedida());
            detalle.put("codTriIGV", item.getCodTipTributoIgv());
            detalle.put("mtoIgvItem", String.valueOf(item.getAfectacionIgv()));
            detalle.put("mtoBaseIgvItem", item.getValorRefUnitario().compareTo(BigDecimal.ZERO) == 0
                    ? Impresion.df.format(item.getPrecioVentaUnitario().multiply(item.getCantidad()).subtract(item.getAfectacionIgv()))
                    : Impresion.df.format(item.getValorRefUnitario().multiply(item.getCantidad())));
            detalle.put("nomTributoIgvItem", item.getAfectacionIGVCode().equals("31") ? "GRA" : "IGV");
            detalle.put("codTipTributoIgvItem", item.getAfectacionIGVCode().equals("31") ? "FRE" : "VAT");
            detalle.put("tipAfeIGV", item.getAfectacionIGVCode());
            detalle.put("porIgvItem", String.valueOf(cabecera.getPorcentajeIGV()));

            detalle.put("codTriISC", "-");
            detalle.put("mtoIscItem", "-");
            detalle.put("mtoBaseIscItem", "-");
            detalle.put("nomTributoIscItem", "-");
            detalle.put("codTipTributoIscItem", "-");
            detalle.put("tipSisISC", "-");
            detalle.put("porIscItem", "-");

            detalle.put("codTriOtro", "-");
            detalle.put("mtoTriOtroItem", "-");
            detalle.put("mtoBaseTriOtroItem", "-");
            detalle.put("nomTributoOtroItem", "-");
            detalle.put("codTipTributoOtroItem", "-");
            detalle.put("porTriOtroItem", "-");

            detalle.put("codTriIcbper", "-");
            detalle.put("mtoTriIcbperItem", "-");
            detalle.put("ctdBolsasTriIcbperItem", "-");
            detalle.put("nomTributoIcbperItem", "-");
            detalle.put("codTipTributoIcbperItem", "-");
            detalle.put("mtoTriIcbperUnidad", "-");

            detalle.put("mtoPrecioVentaUnitario", item.getPrecioVentaUnitario());
            detalle.put("mtoValorVentaItem",
                    Impresion.df.format(item.getPrecioVentaUnitario().multiply(item.getCantidad())
                            .subtract(item.getAfectacionIgv())));
            detalle.put("mtoValorReferencialUnitario", Impresion.df.format(item.getValorRefUnitario()));
            detalle.put("ctdUnidadItem", Impresion.df.format(item.getCantidad()));
            detalle.put("lineaSwf", String.valueOf(contadorItem));
            detalle.put("tipoCodiMoneGratiSwf", "02");

            listaDetalle.add(detalle);
        }

//            }
        factura.put("listaDetalle", listaDetalle);

        List<Map<String, Object>> listaTributos = new ArrayList<>();
        Map<String, Object> tributo = null;
        if (cabecera.getListFacturaElectronicaTax() != null) {
            for (FacturaElectronicaTax itemTax : cabecera.getListFacturaElectronicaTax()) {
                tributo = new HashMap<>();
                tributo.put("ideTributo", String.valueOf(itemTax.getTaxId()));
                tributo.put("nomTributo", itemTax.getNomTributo());
                tributo.put("codTipTributo", itemTax.getCodTipTributo());
                tributo.put("mtoBaseImponible", Impresion.df.format(itemTax.getMtoBaseImponible()));
                tributo.put("mtoTributo", Impresion.df.format(itemTax.getMtoTributo()));
                tributo.put("codigoMonedaSolesSwf", cabecera.getMoneda());

                listaTributos.add(tributo);
            }
            factura.put("listaTributos", listaTributos);
        }
        List<Map<String, Object>> listaCuotas = new ArrayList<>();
        Map<String, Object> cuotaMap = null;
        if (cabecera.getListFacturaElectronicaCuotas() != null) {
            int contador = 0;
            for (FacturaElectronicaCuotas cuota : cabecera.getListFacturaElectronicaCuotas()) {
                contador++;
                cuotaMap = new HashMap<>();
                cuotaMap.put("lineaCuota", "Cuota" + String.format("%03d", contador));
                cuotaMap.put("tipMonedaCuotaPago", cuota.getTipMonedaCuotaPago());
                cuotaMap.put("mtoCuotaPago", String.valueOf(cuota.getMtoCuotaPago()));
                cuotaMap.put("fecCuotaPago", new SimpleDateFormat("yyyy-MM-dd").format(cuota.getFecCuotaPago()));
                listaCuotas.add(cuotaMap);
            }

            factura.put("listaCuotas", listaCuotas);
        }
        List<Map<String, Object>> listaRelaFactura = new ArrayList<>();
        List<Map<String, Object>> listaVariablesGlobales = new ArrayList<>();
        Map<String, Object> relacionadoFactura = null;
        Map<String, Object> relacionadoLista = null;
        if (cabecera.getDocrefMonto() != null && cabecera.getDocrefMonto().compareTo(BigDecimal.ZERO) == 1) {

            //        relacionadoLista = listaRelacionado.next();
            String indDocRelacionado = "2";

            String numIdeAnticipo = "1";

            String tipDocRelacionado = "03";

            String numDocRelacionado = cabecera.getDocrefSerie() + "-" + String.format("%08d", Integer.parseInt(cabecera.getDocrefNumero()));

            String tipDocEmisor = "6";

            String numDocEmisor = this.contri.getNumdoc();

            relacionadoFactura = new HashMap<>();
            relacionadoFactura.put("indDocRelacionado", indDocRelacionado);
            relacionadoFactura.put("numIdeAnticipo", numIdeAnticipo);
            relacionadoFactura.put("tipDocRelacionado", tipDocRelacionado);
            relacionadoFactura.put("numDocRelacionado", numDocRelacionado);
            relacionadoFactura.put("tipDocEmisor", tipDocEmisor);
            relacionadoFactura.put("numDocEmisor", numDocEmisor);
            relacionadoFactura.put("mtoDocRelacionado", sumTotalAnticipos);

            listaRelaFactura.add(relacionadoFactura);

//            Iterator<Map<String, Object>> listaVariableSGlobalesBloque = listaVariablesGlobalesJson.iterator();
            Map<String, Object> variableGlobalJson = null;
            Map<String, Object> variableGlobalMap = null;

//            String tipVariableGlobal = (variableGlobalJson.get("tipVariableGlobal") != null) ? (String) variableGlobalJson.get("tipVariableGlobal") : "";
//
//            String codTipoVariableGlobal = (variableGlobalJson.get("codTipoVariableGlobal") != null) ? (String) variableGlobalJson.get("codTipoVariableGlobal") : "";
//
//            String porVariableGlobal = (variableGlobalJson.get("porVariableGlobal") != null) ? (String) variableGlobalJson.get("porVariableGlobal") : "";
//
//            String monMontoVariableGlobal = (variableGlobalJson.get("monMontoVariableGlobal") != null) ? (String) variableGlobalJson.get("monMontoVariableGlobal") : "";
//
//            String mtoVariableGlobal = (variableGlobalJson.get("mtoVariableGlobal") != null) ? (String) variableGlobalJson.get("mtoVariableGlobal") : "";
//
//            String monBaseImponibleVariableGlobal = (variableGlobalJson.get("monBaseImponibleVariableGlobal") != null) ? (String) variableGlobalJson.get("monBaseImponibleVariableGlobal") : "";
//
//            String mtoBaseImpVariableGlobal = (variableGlobalJson.get("mtoBaseImpVariableGlobal") != null) ? (String) variableGlobalJson.get("mtoBaseImpVariableGlobal") : "";
            variableGlobalMap = new HashMap<>();

            variableGlobalMap.put("tipVariableGlobal", "false");
            variableGlobalMap.put("codTipoVariableGlobal", "04");
            variableGlobalMap.put("porVariableGlobal", "1");
            variableGlobalMap.put("monMontoVariableGlobal", "PEN");
            variableGlobalMap.put("mtoVariableGlobal", sumTotalAnticipos);
            variableGlobalMap.put("monBaseImponibleVariableGlobal", "PEN");
            variableGlobalMap.put("mtoBaseImpVariableGlobal", sumTotalAnticipos);

            listaVariablesGlobales.add(variableGlobalMap);

        }
        factura.put("listaRelacionado", listaRelaFactura);
        factura.put("listaVariablesGlobales", listaVariablesGlobales);
        
        //Leyendas para detraccion
        if (cabecera.getListFacturaElectronicaLeyendas()!= null) {
            List<Map<String, Object>> listaLeyendas = new ArrayList<>(); 
            for (FacturaElectronicaLeyenda leyenda : cabecera.getListFacturaElectronicaLeyendas()) {
                
                Map<String, Object> leyendaMap = null;
                leyendaMap = new HashMap<>();
                leyendaMap.put("codigo", leyenda.getCodigo());
                leyendaMap.put("descripcion", leyenda.getDescripcion());

                listaLeyendas.add(leyendaMap);
            }
            factura.put("listaLeyendas", listaLeyendas);
        }
        
        log.debug("SoftwareFacturadorController.formatoResumenBajas...Fin Procesamiento");
        formatoComunes(factura, this.archivoRelacionado, this.archivoAdiCabecera, this.archivoAdiDetalle, this.archivoLeyendas, this.archivoAdiTributos, this.archivoAdiVariableGlobal);
        return factura;
    }

    public byte[] parse(String templatesPath) throws ParserException {
        return parse(templatesPath, plantillaSeleccionada);
    }
}


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\facturador\parser\pipe\PipeFacturaParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */

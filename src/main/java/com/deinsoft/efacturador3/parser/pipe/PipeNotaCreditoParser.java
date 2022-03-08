package com.deinsoft.efacturador3.parser.pipe;

import com.deinsoft.efacturador3.model.Contribuyente;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.parser.Parser;
import com.deinsoft.efacturador3.parser.ParserException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipeNotaCreditoParser
        extends PipeCpeAbstractParser
        implements Parser {

    private static final Logger log = LoggerFactory.getLogger(PipeNotaCreditoParser.class);

    private static String plantillaSeleccionada = "ConvertirNCreditoXML.ftl";

    private String archivoCabecera;

    private String archivoDetalle;

    private String nombreArchivo;

    private Empresa contri;

    private String archivoRelacionado = "";

    private String archivoAdiCabecera = "";

    private String archivoAdiDetalle = "";

    private String archivoLeyendas = "";

    private String archivoAdiTributos = "";

    private String archivoAdiVariableGlobal = "";

    private FacturaElectronica cabecera;

    /*     */

    public PipeNotaCreditoParser(Empresa contri, FacturaElectronica cabecera) {
        this.contri = contri;
        this.cabecera = cabecera;
    }

    public Map<String, Object> pipeToMap() throws ParserException {
        log.debug("SoftwareFacturadorController.formatoNotaCredito...Inicio Procesamiento");

//        String[] datosArchivo = this.nombreArchivo.split("\\-");

        String identificadorFirmaSwf = "SIGN";
        Random calcularRnd = new Random();
        Integer codigoFacturadorSwf = Integer.valueOf((int) (calcularRnd.nextDouble() * 1000000.0D));

        log.debug("SoftwareFacturadorController.formatoResumenBajas...Leyendo Archivo: " + this.archivoCabecera);
        Map<String, Object> notaCredito = new HashMap<>();

//    Path fileCabecera = Paths.get(this.archivoCabecera, new String[0]);
//    
//    if (!Files.exists(fileCabecera, new java.nio.file.LinkOption[0]))
//    {
//      throw new ParserException("El archivo no existe: " + this.archivoCabecera);
//    }
//    try(InputStream in = Files.newInputStream(fileCabecera, new java.nio.file.OpenOption[0]); 
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
//      String cadena = null;
//      
//      while ((cadena = reader.readLine()) != null) {
//        String[] registro = cadena.split("\\|");
//        if (registro.length != 21) {
//          throw new ParserException("El archivo cabecera no continene la cantidad de datos esperada (21 columnas).");
//        }
        notaCredito = new HashMap<>();
        notaCredito.put("tipOperacion", cabecera.getTipoOperacion());
        notaCredito.put("fecEmision", new SimpleDateFormat("yyyy-MM-dd").format(cabecera.getFechaEmision()));
        notaCredito.put("horEmision", "12:00:00");
        notaCredito.put("codLocalEmisor", cabecera.getCodLocal());
        notaCredito.put("tipDocUsuario", cabecera.getClienteTipo());
        notaCredito.put("numDocUsuario", cabecera.getClienteDocumento());
        notaCredito.put("rznSocialUsuario", cabecera.getClienteNombre());
        notaCredito.put("moneda", "PEN");

        notaCredito.put("codMotivo", cabecera.getNotaTipo());
        notaCredito.put("desMotivo", cabecera.getNotaMotivo());
        notaCredito.put("tipDocAfectado", cabecera.getNotaReferenciaTipo());
        notaCredito.put("numDocAfectado", cabecera.getNotaReferenciaSerie() + "-" + String.format("%08d", Integer.parseInt(cabecera.getNotaReferenciaNumero())));

        notaCredito.put("sumTotTributos", cabecera.getSumatoriaIGV().add(cabecera.getSumatoriaISC()));
        notaCredito.put("sumTotValVenta", cabecera.getTotalValorVenta().subtract(cabecera.getSumatoriaIGV()));
        notaCredito.put("sumPrecioVenta", cabecera.getTotalValorVenta());
        notaCredito.put("sumDescTotal", cabecera.getDescuentosGlobales() == null ? "0.00" : cabecera.getDescuentosGlobales());
        notaCredito.put("sumOtrosCargos", cabecera.getSumatoriaOtrosCargos() == null ? "0.00" : cabecera.getSumatoriaOtrosCargos());
        notaCredito.put("sumTotalAnticipos", 0);
        notaCredito.put("sumImpVenta", cabecera.getTotalValorVenta());

        notaCredito.put("ublVersionId", "2.1");
        notaCredito.put("customizationId", cabecera.getCustomizationId());

        notaCredito.put("ublVersionIdSwf", "2.0");
        notaCredito.put("CustomizationIdSwf", "1.0");
        notaCredito.put("nroCdpSwf", cabecera.getSerie() + "-" + String.format("%08d", Integer.parseInt(cabecera.getNumero())));
        notaCredito.put("tipCdpSwf", cabecera.getTipo());
        notaCredito.put("nroRucEmisorSwf", this.contri.getNumdoc());
        notaCredito.put("tipDocuEmisorSwf", cabecera.getEmpresa().getTipodoc().toString());
        notaCredito.put("nombreComercialSwf", this.contri.getRazonSocial());
        notaCredito.put("razonSocialSwf", this.contri.getRazonSocial());
//        notaCredito.put("ubigeoDomFiscalSwf", this.contri.getDireccion().getUbigeo());
//        notaCredito.put("direccionDomFiscalSwf", this.contri.getDireccion().getDireccion());
//        notaCredito.put("deparSwf", this.contri.getDireccion().getDepar());
//        notaCredito.put("provinSwf", this.contri.getDireccion().getProvin());
//        notaCredito.put("distrSwf", this.contri.getDireccion().getDistr());
//        notaCredito.put("urbanizaSwf", this.contri.getDireccion().getUrbaniza());
        notaCredito.put("ubigeoDomFiscalSwf", "");
        notaCredito.put("direccionDomFiscalSwf", "");
        notaCredito.put("deparSwf", "");
        notaCredito.put("provinSwf", "");
        notaCredito.put("distrSwf", "");
        notaCredito.put("urbanizaSwf", "");
        notaCredito.put("paisDomFiscalSwf", "PE");
        notaCredito.put("codigoMontoDescuentosSwf", "2005");
        notaCredito.put("codigoMontoOperGravadasSwf", "1001");
        notaCredito.put("codigoMontoOperInafectasSwf", "1002");
        notaCredito.put("codigoMontoOperExoneradasSwf", "1003");
        notaCredito.put("idIgv", "1000");
        notaCredito.put("codIgv", "IGV");
        notaCredito.put("codExtIgv", "VAT");
        notaCredito.put("idIsc", "2000");
        notaCredito.put("codIsc", "ISC");
        notaCredito.put("codExtIsc", "EXC");
        notaCredito.put("idOtr", "9999");
        notaCredito.put("codOtr", "OTROS");
        notaCredito.put("codExtOtr", "OTH");
        notaCredito.put("tipoCodigoMonedaSwf", "01");
        notaCredito.put("identificadorFacturadorSwf", "SISTEMA FACTURADOR DEFACTURADOR - DEINSOFT SRL");
        notaCredito.put("codigoFacturadorSwf", codigoFacturadorSwf.toString());
        notaCredito.put("identificadorFirmaSwf", identificadorFirmaSwf);

//      }
//
//    
//    }
//    catch (IOException x) {
//      throw new ParserException("No se pudo leer el archivo cabecera: " + this.archivoCabecera, x);
//    } 
//    Path fileDetalle = Paths.get(this.archivoDetalle, new String[0]);
//    
//    if (!Files.exists(fileDetalle, new java.nio.file.LinkOption[0]))
//    {
//      throw new ParserException("El archivo no existe: " + this.archivoDetalle);
//    }
        List<Map<String, Object>> listaDetalle = new ArrayList<>();
        Map<String, Object> detalle = null;

//    try(InputStream in = Files.newInputStream(fileDetalle, new java.nio.file.OpenOption[0]); 
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
//      String cadena = null;
//      
        Integer linea = Integer.valueOf(0);
//      
//      while ((cadena = reader.readLine()) != null) {
//        String[] registro = cadena.split("\\|");
//        if (registro.length != 36) {
//          throw new ParserException("El archivo detalle no continene la cantidad de datos esperada (30 columnas).");
//        }

        for (FacturaElectronicaDet item : cabecera.getListFacturaElectronicaDet()) {
            linea = Integer.valueOf(linea.intValue() + 1);
            detalle = new HashMap<>();
            detalle.put("unidadMedida", item.getUnidadMedida());
            detalle.put("ctdUnidadItem", String.valueOf(item.getCantidad()));
            detalle.put("codProducto", item.getId());
            //        detalle.put("codProductoSUNAT", registro[3]);
            detalle.put("desItem", item.getDescripcion());
            detalle.put("mtoValorUnitario", item.getValorUnitario());

            detalle.put("sumTotTributosItem", String.valueOf(item.getAfectacionIgv()));

            detalle.put("codTriIGV", "1000");
            detalle.put("mtoIgvItem", String.valueOf(item.getAfectacionIgv()));
            detalle.put("mtoBaseIgvItem", item.getPrecioVentaUnitario().multiply(item.getCantidad()).subtract(item.getAfectacionIgv()));
            detalle.put("nomTributoIgvItem", "IGV");
            detalle.put("codTipTributoIgvItem", "VAT");
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
            detalle.put("mtoValorVentaItem", String.valueOf(item.getValorVentaItem()));
            detalle.put("mtoValorReferencialUnitario", "0.00");
            detalle.put("lineaSwf", String.valueOf(linea));
            detalle.put("tipoCodiMoneGratiSwf", "02");

            listaDetalle.add(detalle);
        }

        notaCredito.put("listaDetalle", listaDetalle);
        List<Map<String, Object>> listaTributos = new ArrayList<>();
        Map<String, Object> tributo = null;
        if(cabecera.getListFacturaElectronicaTax() != null){
            for (FacturaElectronicaTax itemTax : cabecera.getListFacturaElectronicaTax()) {
                tributo = new HashMap<>();
                tributo.put("ideTributo",String.valueOf(itemTax.getTaxId()));
                tributo.put("nomTributo", "IGV");
                tributo.put("codTipTributo", "VAT");
                tributo.put("mtoBaseImponible", itemTax.getBaseamt().toString());
                tributo.put("mtoTributo", itemTax.getTaxtotal().toString());
                tributo.put("codigoMonedaSolesSwf",cabecera.getMoneda());

                listaTributos.add(tributo);
            }
            notaCredito.put("listaTributos", listaTributos);
        }
        formatoComunes(notaCredito, this.archivoRelacionado, this.archivoAdiCabecera, this.archivoAdiDetalle, this.archivoLeyendas, this.archivoAdiTributos, this.archivoAdiVariableGlobal);

        log.debug("SoftwareFacturadorController.formatoNotaCredito...Fin Procesamiento");

        return notaCredito;
    }

    public byte[] parse(String templatesPath) throws ParserException {
        return parse(templatesPath, plantillaSeleccionada);
    }
}

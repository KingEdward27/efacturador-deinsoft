package com.deinsoft.efacturador3.parser.pipe;

import com.deinsoft.efacturador3.model.Contribuyente;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.parser.Parser;
import com.deinsoft.efacturador3.parser.ParserException;
import com.deinsoft.efacturador3.util.Impresion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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

    public PipeNotaCreditoParser(Empresa contri, FacturaElectronica cabecera) {
        this.contri = contri;
        this.cabecera = cabecera;
    }

    public Map<String, Object> pipeToMap() throws ParserException {
        log.debug("SoftwareFacturadorController.formatoNotaCredito...Inicio Procesamiento");

        String identificadorFirmaSwf = "SIGN";
        Random calcularRnd = new Random();
        Integer codigoFacturadorSwf = Integer.valueOf((int) (calcularRnd.nextDouble() * 1000000.0D));

        log.debug("SoftwareFacturadorController.formatoResumenBajas...Leyendo Archivo: " + this.archivoCabecera);
        Map<String, Object> notaCredito = new HashMap<>();

        notaCredito = new HashMap<>();
        notaCredito.put("tipOperacion", cabecera.getTipoOperacion());
        notaCredito.put("fecEmision", cabecera.getFechaEmision().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        notaCredito.put("horEmision", "12:00:00");
        notaCredito.put("codLocalEmisor", cabecera.getCodLocal());
        notaCredito.put("tipDocUsuario", cabecera.getClienteTipo());
        notaCredito.put("numDocUsuario", cabecera.getClienteDocumento());
        notaCredito.put("rznSocialUsuario", cabecera.getClienteNombre());
        notaCredito.put("moneda", cabecera.getMoneda());

        notaCredito.put("codMotivo", cabecera.getNotaTipo());
        notaCredito.put("desMotivo", cabecera.getNotaMotivo());
        notaCredito.put("tipDocAfectado", cabecera.getNotaReferenciaTipo());
        notaCredito.put("numDocAfectado", cabecera.getNotaReferenciaSerie() + "-" + String.format("%08d", Integer.parseInt(cabecera.getNotaReferenciaNumero())));

        notaCredito.put("sumTotTributos", Impresion.df.format(cabecera.getSumatoriaIGV().add(cabecera.getSumatoriaISC())));
//        notaCredito.put("sumTotValVenta", Impresion.df.format(cabecera.getTotalValorVenta().subtract(cabecera.getSumatoriaIGV())));
//        notaCredito.put("sumPrecioVenta", Impresion.df.format(cabecera.getTotalValorVenta()));
        notaCredito.put("sumTotValVenta", Impresion.df.format(cabecera.getTotalValorVentasGravadas().subtract(cabecera.getDescuentosGlobales()).subtract(cabecera.getSumatoriaIGV())));
        notaCredito.put("sumPrecioVenta", Impresion.df.format(cabecera.getTotalValorVentasGravadas().subtract(cabecera.getDescuentosGlobales())));

        //notaCredito.put("sumDescTotal", cabecera.getDescuentosGlobales() == null ? "0.00" : cabecera.getDescuentosGlobales());
        notaCredito.put("sumDescTotal","0.00");
        notaCredito.put("sumOtrosCargos", cabecera.getSumatoriaOtrosCargos() == null ? "0.00" : cabecera.getSumatoriaOtrosCargos());
        notaCredito.put("sumTotalAnticipos", 0);
        notaCredito.put("sumImpVenta", Impresion.df.format(cabecera.getTotalValorVenta()));

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

        List<Map<String, Object>> listaDetalle = new ArrayList<>();
        Map<String, Object> detalle = null;
        Map<String, Object> adicionalDetalle = null;
        List<Map<String, Object>> listaAdicionalDetalle = new ArrayList<>();

        Integer linea = Integer.valueOf(0);
//      
        if (cabecera.getListFacturaElectronicaDet() != null) {
            int contadorItem = 0;
            for (FacturaElectronicaDet item : cabecera.getListFacturaElectronicaDet()) {
                contadorItem++;
                BigDecimal descuentoReal = item.getDescuento()
                        .multiply(BigDecimal.ONE.add(cabecera.getPorcentajeIGV()
                                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)));
                linea = Integer.valueOf(linea.intValue() + 1);
                detalle = new HashMap<>();

                detalle.put("codProducto", item.getCodigo());
                detalle.put("desItem", item.getDescripcion());
                detalle.put("mtoValorUnitario", item.getValorUnitario().subtract(
                        item.getDescuento()
                        .divide(item.getCantidad(), 4, RoundingMode.HALF_UP)).doubleValue());

                detalle.put("sumTotTributosItem", String.valueOf(item.getAfectacionIgv()));
                detalle.put("unidadMedida", item.getUnidadMedida());
                detalle.put("codTriIGV", item.getCodTipTributoIgv());
                detalle.put("mtoIgvItem", String.valueOf(item.getAfectacionIgv()));

                detalle.put("mtoBaseIgvItem", item.getValorRefUnitario().compareTo(BigDecimal.ZERO) == 0
                        ? Impresion.df.format(item.getValorVentaItem())
                        : Impresion.df.format(item.getValorRefUnitario().multiply(item.getCantidad())));

//                detalle.put("mtoBaseIgvItem", item.getValorRefUnitario().compareTo(BigDecimal.ZERO) == 0
//                        ? Impresion.df.format(item.getPrecioVentaUnitario().multiply(item.getCantidad()).subtract(item.getAfectacionIgv()))
//                        : Impresion.df.format(item.getValorRefUnitario().multiply(item.getCantidad())));
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

//                detalle.put("mtoValorVentaItem",
//                        Impresion.df.format(item.getPrecioVentaUnitario().multiply(item.getCantidad())
//                                .subtract(item.getAfectacionIgv())));
                detalle.put("mtoValorVentaItem", Impresion.df.format(item.getValorVentaItem()));

//                detalle.put("mtoPrecioVentaUnitario", item.getPrecioVentaUnitario());
                detalle.put("mtoPrecioVentaUnitario", Impresion.df.format(
                        item.getPrecioVentaUnitario()
                                .subtract(descuentoReal.divide(item.getCantidad(), 4, RoundingMode.HALF_UP))
                ));
                detalle.put("mtoValorReferencialUnitario", Impresion.df.format(item.getValorRefUnitario()));
                detalle.put("ctdUnidadItem", Impresion.df.format(item.getCantidad()));
                detalle.put("lineaSwf", String.valueOf(contadorItem));
                detalle.put("tipoCodiMoneGratiSwf", "02");
                listaDetalle.add(detalle);

//                if (item.getDescuento().compareTo(BigDecimal.ZERO) > 0) {
//                    adicionalDetalle = new HashMap<>();
//                    adicionalDetalle.put("idLinea", String.valueOf(contadorItem));
//                    adicionalDetalle.put("nomPropiedad", "");
//
//                    adicionalDetalle.put("tipVariable", "false");
//                    adicionalDetalle.put("codTipoVariable", "00");
//                    adicionalDetalle.put("porVariable", Impresion.DF_0000.format(item.getDescuento()
//                            .divide(item.getValorVentaItem().add(item.getDescuento()), 4, RoundingMode.HALF_UP)));
//                    adicionalDetalle.put("monMontoVariable", "PEN");
//                    adicionalDetalle.put("mtoVariable", Impresion.df.format(item.getDescuento()));
//                    adicionalDetalle.put("monBaseImponibleVariable","PEN");
//                    adicionalDetalle.put("mtoBaseImpVariable",
//                            Impresion.df.format(item.getValorVentaItem().add(item.getDescuento())));
//
//                    listaAdicionalDetalle.add(adicionalDetalle);
//                }
            }
            notaCredito.put("listaDetalle", listaDetalle);
//            notaCredito.put("listaAdicionalDetalle", listaAdicionalDetalle);
        } else {
            notaCredito.put("listaDetalle", new ArrayList<>());
//            notaCredito.put("listaAdicionalDetalle", new ArrayList<>());
        }

        List<Map<String, Object>> listaTributos = new ArrayList<>();
        Map<String, Object> tributo = null;
        if (cabecera.getListFacturaElectronicaTax() != null) {
            for (FacturaElectronicaTax itemTax : cabecera.getListFacturaElectronicaTax()) {
                tributo = new HashMap<>();
//                tributo.put("ideTributo", String.valueOf(itemTax.getTaxId()));
//                tributo.put("nomTributo", itemTax.getNomTributo());
//                tributo.put("codTipTributo", itemTax.getCodTipTributo());
//                tributo.put("mtoBaseImponible", Impresion.df.format(itemTax.getMtoBaseImponible()));
//                tributo.put("mtoTributo", Impresion.df.format(itemTax.getMtoTributo()));
//                tributo.put("codigoMonedaSolesSwf", cabecera.getMoneda());

                tributo.put("ideTributo", String.valueOf(itemTax.getTaxId()));
                tributo.put("nomTributo", itemTax.getNomTributo());
                tributo.put("codTipTributo", itemTax.getCodTipTributo());
                tributo.put("mtoBaseImponible", Impresion.df.format(itemTax.getMtoBaseImponible()));
                tributo.put("mtoTributo", Impresion.df.format(itemTax.getMtoTributo()));
                tributo.put("codigoMonedaSolesSwf", cabecera.getMoneda());
                listaTributos.add(tributo);
            }
            notaCredito.put("listaTributos", listaTributos);
        }
        List<Map<String, Object>> listaRelacionado = new ArrayList<>();
        notaCredito.put("listaRelacionado", listaRelacionado); 

        List<Map<String, Object>> listaVariablesGlobales = new ArrayList<>();
        notaCredito.put("listaVariablesGlobales", listaVariablesGlobales);
//        notaCredito.put("listaTributos", new ArrayList<>());
        formatoComunes(notaCredito, this.archivoRelacionado, this.archivoAdiCabecera, this.archivoAdiDetalle, this.archivoLeyendas, this.archivoAdiTributos, this.archivoAdiVariableGlobal);

        log.debug("SoftwareFacturadorController.formatoNotaCredito...Fin Procesamiento");

        return notaCredito;
    }

    public byte[] parse(String templatesPath) throws ParserException {
        return parse(templatesPath, plantillaSeleccionada);
    }
}

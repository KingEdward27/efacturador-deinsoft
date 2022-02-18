package com.deinsoft.efacturador3.parser.pipe;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ComprobanteDet;
import com.deinsoft.efacturador3.bean.Empresa;
import com.deinsoft.efacturador3.bean.FacturaElectronica;
import com.deinsoft.efacturador3.bean.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.Contribuyente;
import com.deinsoft.efacturador3.parser.Parser;
import com.deinsoft.efacturador3.parser.ParserException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

    private FacturaElectronicaDet detalle;

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

                factura = new HashMap<>();
                factura.put("tipOperacion", cabecera.getTipoOperacion());
                factura.put("fecEmision", new SimpleDateFormat("dd/MM/yyyy").format(cabecera.getFechaEmision()));
                factura.put("horEmision", "");
                factura.put("fecVencimiento", new SimpleDateFormat("dd/MM/yyyy").format(cabecera.getFechaEmision()));
                factura.put("codLocalEmisor", "");
                factura.put("tipDocUsuario", cabecera.getClienteTipo());
                factura.put("numDocUsuario", cabecera.getClienteDocumento());
                factura.put("rznSocialUsuario", cabecera.getClienteNombre());
                factura.put("moneda", "PEN");
                
                factura.put("sumTotTributos", cabecera.getSumatoriaIGV().add(cabecera.getSumatoriaISC()));
                factura.put("sumTotValVenta", cabecera.getTotalValorVenta());
                factura.put("sumPrecioVenta", cabecera.getTotalValorVenta());
                factura.put("sumDescTotal", cabecera.getDescuentosGlobales()==null?"0.00":cabecera.getDescuentosGlobales());
                factura.put("sumOtrosCargos", cabecera.getSumatoriaOtrosCargos()==null?"0.00":cabecera.getSumatoriaOtrosCargos());
                factura.put("sumTotalAnticipos", 0);
                factura.put("sumImpVenta", cabecera.getTotalValorVenta());

                factura.put("ublVersionId", "2.1");
                factura.put("customizationId", cabecera.getCustomizationId());

                factura.put("ublVersionIdSwf", "2.0");
                factura.put("CustomizationIdSwf", "1.0");
                factura.put("nroCdpSwf", cabecera.getNumero());
                factura.put("tipCdpSwf", cabecera.getTipo());
                factura.put("nroRucEmisorSwf", this.contri.getNumdoc());
                factura.put("tipDocuEmisorSwf", "6");
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
                factura.put("identificadorFacturadorSwf", "Elaborado por Sistema de Emision Electronica Facturador SUNAT (SEE-SFS) 1.3.2");
                factura.put("codigoFacturadorSwf", codigoFacturadorSwf.toString());
                factura.put("identificadorFirmaSwf", identificadorFirmaSwf);
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
        for (FacturaElectronicaDet item : cabecera.getListFacturaElectronicaDet()) {
//            linea = Integer.valueOf(linea.intValue() + 1);
                detalle = new HashMap<>();
                detalle.put("unidadMedida", item.getUnidadMedida());
                detalle.put("ctdUnidadItem", item.getCantidad());
                detalle.put("codProducto", item.getId());
                detalle.put("codProductoSUNAT", "-");
                detalle.put("desItem", item.getDescripcion());
                detalle.put("mtoValorUnitario", item.getValorUnitario());

                detalle.put("sumTotTributosItem", "");

                detalle.put("codTriIGV", item.getAfectacionIGVCode());
                detalle.put("mtoIgvItem", item.getAfectacionIgv());
                detalle.put("mtoBaseIgvItem", item.getPrecioVentaUnitario().multiply(item.getCantidad()).subtract(item.getAfectacionIgv()));
                detalle.put("nomTributoIgvItem", "igv");
                detalle.put("codTipTributoIgvItem", item.getCodTipTributoIgv());
                detalle.put("tipAfeIGV", item.getAfectacionIGVCode());
                detalle.put("porIgvItem", "0.18");

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
                detalle.put("mtoValorVentaItem", item.getValorVentaItem());

                detalle.put("mtoValorReferencialUnitario",item.getValorUnitario());

                detalle.put("lineaSwf", item.getId());
                detalle.put("tipoCodiMoneGratiSwf", "02");

                listaDetalle.add(detalle);
        }
                
//            }

            factura.put("listaDetalle", listaDetalle);

            formatoComunes(factura, this.archivoRelacionado, this.archivoAdiCabecera, this.archivoAdiDetalle, this.archivoLeyendas, this.archivoAdiTributos, this.archivoAdiVariableGlobal);

//        } catch (IOException x) {
//            throw new ParserException("No se pudo leer el archivo detalle: " + this.archivoDetalle, x);
//        }

        log.debug("SoftwareFacturadorController.formatoResumenBajas...Fin Procesamiento");

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

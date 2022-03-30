/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ComprobanteCuotas;
import com.deinsoft.efacturador3.bean.ComprobanteDet;
import com.deinsoft.efacturador3.bean.MailBean;
import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.config.XsltCpePath;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaCuotas;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.repository.ErrorRepository;
import com.deinsoft.efacturador3.repository.FacturaElectronicaRepository;
import com.deinsoft.efacturador3.service.ComunesService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.GenerarDocumentosService;
import com.deinsoft.efacturador3.soap.gencdp.ExceptionDetail;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import com.deinsoft.efacturador3.util.Catalogos;
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import com.deinsoft.efacturador3.util.Impresion;
import com.deinsoft.efacturador3.util.SendMail;
import com.deinsoft.efacturador3.validator.XsdCpeValidator;
import com.deinsoft.efacturador3.validator.XsltCpeValidator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author EDWARD-PC
 */
@Service
public class FacturaElectronicaServiceImpl implements FacturaElectronicaService {

    private static final Logger log = LoggerFactory.getLogger(BandejaDocumentosServiceImpl.class);

    @Autowired
    FacturaElectronicaRepository facturaElectronicaRepository;

    @Autowired
    private ComunesService comunesService;

    @Autowired
    private GenerarDocumentosService generarDocumentosService;

    @Autowired
    private ErrorRepository errorDao;

    @Autowired
    private XsltCpePath xsltCpePath;

    @Autowired
    AppConfig appConfig;

    @Override
    public FacturaElectronica getById(long id) {
        return facturaElectronicaRepository.getById(id);
    }

    @Override
    public List<FacturaElectronica> getListFacturaElectronica() {
        return facturaElectronicaRepository.findAll();
    }

    @Override
    public FacturaElectronica save(FacturaElectronica facturaElectronica) {
        return facturaElectronicaRepository.save(facturaElectronica);
    }

    @Override
    public List<FacturaElectronica> getBySerieAndNumero(FacturaElectronica facturaElectronica) {
        return facturaElectronicaRepository.findBySerieAndNumero(facturaElectronica.getSerie(), facturaElectronica.getNumero());
    }

    @Override
    public List<FacturaElectronica> getByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(FacturaElectronica facturaElectronica) {
        return facturaElectronicaRepository.findByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(facturaElectronica);
    }

    

    @Override
    @Transactional
    public Map<String, Object> generarComprobantePagoSunat(String rootpath, FacturaElectronica documento) throws TransferirArchivoException {
        XsltCpeValidator xsltCpeValidator = new XsltCpeValidator(this.comunesService, this.errorDao, this.xsltCpePath);
        XsdCpeValidator xsdCpeValidator = new XsdCpeValidator(this.comunesService, this.errorDao, this.xsltCpePath);
        Map<String, Object> retorno = new HashMap<>();
        FacturaElectronica facturaElectronicaResult = null;
        long ticket = Calendar.getInstance().getTimeInMillis();
        retorno.put("ticketOperacion", ticket);
        try {
//            String retorno = "01";
            String tipoComprobante = null;
            String nomFile = "";

            facturaElectronicaResult = save(documento);

            if (Constantes.CONSTANTE_SITUACION_POR_GENERAR_XML.equals(documento.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_CON_ERRORES.equals(documento.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_XML_VALIDAR.equals(documento.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_ENVIADO_RECHAZADO.equals(documento.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_ENVIADO_ANULADO.equals(documento.getIndSituacion())) {
//                retorno = "";
                tipoComprobante = documento.getTipo();
                log.debug("BandejaDocumentosServiceImpl.generarComprobantePagoSunat...tipoComprobante: " + tipoComprobante);

                nomFile = documento.getEmpresa().getNumdoc()
                        + "-" + String.format("%02d", Integer.parseInt(documento.getTipo()))
                        + "-" + documento.getSerie()
                        + "-" + String.format("%08d", Integer.parseInt(documento.getNumero()));
                this.generarDocumentosService.formatoPlantillaXml(rootpath, documento, nomFile);

                retorno.putAll(this.generarDocumentosService.firmarXml(rootpath, documento, nomFile));

                xsdCpeValidator.validarSchemaXML(rootpath, documento, rootpath + "/" + documento.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml");

                xsltCpeValidator.validarXMLYComprimir(rootpath, documento, rootpath + "/" + documento.getEmpresa().getNumdoc() + "/PARSE/", nomFile);

                documento.setXmlHash(retorno.get("xmlHash").toString());
                String tempDescripcionPluralMoneda = "SOLES";
                ByteArrayInputStream stream = Impresion.Imprimir(rootpath + "TEMP/", 1, documento, tempDescripcionPluralMoneda);
                int n = stream.available();
                byte[] bytes = new byte[n];
                stream.read(bytes, 0, n);

                retorno.put("pdfBase64", bytes);

                FileUtils.writeByteArrayToFile(new File(rootpath + "TEMP/" + nomFile + ".pdf"), bytes);

                if (!appConfig.getEnvironment().equals("PRODUCTION")) {
                    FileUtils.writeByteArrayToFile(new File("D:/report.pdf"), bytes);
                }
                String[] adjuntos = {rootpath + "/" + documento.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml",
                    rootpath + "TEMP/" + nomFile + ".pdf"};

                if (!facturaElectronicaResult.getClienteEmail().equals("")) {
                    if (SendMail.validaCorreo(nomFile)) {
                        throw new Exception("Formato de correo inválido, si no se desea enviar correo al cliente dejar en blanco");
                    }
                    String nroDocumento = documento.getSerie() + "-" + String.format("%08d", Integer.parseInt(documento.getNumero()));
                    String cuerpo = "Estimado Cliente, \n\n"
                            + "Informamos a usted que el documento " + nroDocumento + " ya se encuentra disponible.  \n"
                            + "Tipo	:	" + Catalogos.tipoDocumento(documento.getTipo(), "")[1].toUpperCase() + " ELECTRÓNICA" + " \n"
                            + "Número	:	" + nroDocumento + "\n"
                            + "Monto	:	S/ " + String.valueOf(documento.getTotalValorVenta()) + "\n"
                            + "Fecha Emisión	:	" + documento.getFechaEmision() + "\n"
                            + "Saluda atentamente, \n\n"
                            + (documento.getEmpresa().getNombreComercial() == null ? documento.getEmpresa().getRazonSocial() : documento.getEmpresa().getNombreComercial());
                    try {
                        SendMail.sendEmail(new MailBean("Comprobante electrónico",
                            cuerpo,
                            appConfig.getSendEmailEmail(),
                            appConfig.getSendEmailPassword(),
                            facturaElectronicaResult.getClienteEmail(),
                            adjuntos));
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.debug("BandejaDocumentosServiceImpl.generarComprobantePagoSunat...SendMail: Correo no enviado" + e.getMessage());
                    }
                    
                }
                facturaElectronicaResult.setFechaGenXml(new Date());
                facturaElectronicaResult.setIndSituacion(Constantes.CONSTANTE_SITUACION_XML_GENERADO);

                facturaElectronicaResult.setTicketOperacion(ticket);
                save(facturaElectronicaResult);

            }
            return retorno;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setMessage(e.getMessage());
            throw new TransferirArchivoException(e.getMessage(), exceptionDetail);
        }
    }
    @Override
    public void sendToSUNAT() {
        List<FacturaElectronica> list = facturaElectronicaRepository.findByIndSituacion(Constantes.CONSTANTE_SITUACION_XML_GENERADO);
        String nombreArchivo = appConfig.getRootPath() + "VALI/" + "constantes.properties";
        Properties prop = this.comunesService.getProperties(nombreArchivo);
        String urlWebService = (prop.getProperty("RUTA_SERV_CDP") != null) ? prop.getProperty("RUTA_SERV_CDP") : "XX";
        list.forEach((facturaElectronica) -> {
            try {
                HashMap<String, String> resultadoWebService = null;
                String filename = facturaElectronica.getEmpresa().getNumdoc()
                        + "-" + String.format("%02d", Integer.parseInt(facturaElectronica.getTipo()))
                        + "-" + facturaElectronica.getSerie()
                        + "-" + String.format("%08d", Integer.parseInt(facturaElectronica.getNumero()));
                log.debug("BandejaDocumentosServiceImpl.enviarComprobantePagoSunat...Validando Conexión a Internet");
                String[] rutaUrl = urlWebService.split("\\/");
                log.debug("BandejaDocumentosServiceImpl.enviarComprobantePagoSunat...tokens: " + rutaUrl[2]);
                this.comunesService.validarConexion(rutaUrl[2], 443);
                log.debug("BandejaDocumentosServiceImpl.enviarComprobantePagoSunat...filename: " + filename);
                resultadoWebService = this.generarDocumentosService.enviarArchivoSunat(urlWebService, appConfig.getRootPath(), filename, facturaElectronica);
                log.debug("BandejaDocumentosServiceImpl.enviarComprobantePagoSunat...enviarComprobantePagoSunat Final");
                if (resultadoWebService != null) {
                    String estadoRetorno = (resultadoWebService.get("situacion") != null) ? (String) resultadoWebService.get("situacion") : "";
                    String mensaje = (resultadoWebService.get("mensaje") != null) ? (String) resultadoWebService.get("mensaje") : "-";
                    if (!"".equals(estadoRetorno)) {
                        if (!Constantes.CONSTANTE_SITUACION_DESCARGA_CDR.equals(estadoRetorno)
                                && !Constantes.CONSTANTE_SITUACION_DESCARGA_CDR_OBSERVACIONES.equals(estadoRetorno)) {
                            facturaElectronica.setFechaEnvio(new Date());
                            facturaElectronica.setIndSituacion(estadoRetorno);
                            facturaElectronica.setObservacionEnvio(mensaje);
                            save(facturaElectronica);
                        } else {
                            facturaElectronica.setIndSituacion(estadoRetorno);
                            facturaElectronica.setObservacionEnvio(mensaje);
                            save(facturaElectronica);
                        }
                    }
                }
            } catch (Exception e) {
                String mensaje = "Hubo un problema al invocar servicio SUNAT: " + e.getMessage();
                e.printStackTrace();
                log.error(mensaje);
                facturaElectronica.setFechaEnvio(new Date());
                facturaElectronica.setIndSituacion(Constantes.CONSTANTE_SITUACION_CON_ERRORES);
                facturaElectronica.setObservacionEnvio(mensaje);
                save(facturaElectronica);
            }

        });
    }
    @Override
    public FacturaElectronica toFacturaModel(ComprobanteCab documento) throws TransferirArchivoException, ParseException {
        BigDecimal totalValorVentasGravadas = BigDecimal.ZERO, totalValorVentasInafectas = BigDecimal.ZERO,
                totalValorVentasExoneradas = BigDecimal.ZERO,
                SumatoriaIGV = BigDecimal.ZERO, SumatoriaISC = BigDecimal.ZERO,
                sumatoriaOtrosTributos = BigDecimal.ZERO, sumatoriaOtrosCargos = BigDecimal.ZERO, totalValorVenta = BigDecimal.ZERO;
        FacturaElectronica comprobante = new FacturaElectronica();
        //constantes o bd
        for (ComprobanteDet detalle : documento.getLista_productos()) {
            if (Integer.valueOf(detalle.getTipo_igv()) >= 10 && Integer.valueOf(detalle.getTipo_igv()) <= 20) {
                totalValorVentasGravadas = totalValorVentasGravadas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            } else if (Integer.valueOf(detalle.getTipo_igv()) >= 30 && Integer.valueOf(detalle.getTipo_igv()) <= 36) {
                totalValorVentasInafectas = totalValorVentasInafectas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            } else {
                totalValorVentasExoneradas = totalValorVentasExoneradas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            }
            SumatoriaIGV = SumatoriaIGV.add(detalle.getAfectacion_igv());
            SumatoriaISC = SumatoriaISC.add(detalle.getAfectacion_isc() == null ? BigDecimal.ZERO : detalle.getAfectacion_isc());
            totalValorVenta = totalValorVenta.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
        }

        documento.setSumatoriaIGV(SumatoriaIGV);
        documento.setSumatoriaISC(SumatoriaISC);
        documento.setSumatoriaOtrosCargos(BigDecimal.ZERO);
        documento.setSumatoriaOtrosTributos(BigDecimal.ZERO);
        documento.setTotalValorVentasGravadas(totalValorVentasGravadas);
        documento.setTotalValorVentasInafectas(totalValorVentasInafectas);
        documento.setTotalValorVentasExoneradas(totalValorVentasExoneradas);

        comprobante.setTipo(documento.getTipo());
        comprobante.setTipoOperacion(documento.getTipo_operacion());
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(documento.getFecha_emision());
        //	java.sql.Date dateSql = new java.sql.Date(date1.getTime());
        comprobante.setFechaEmision(date1);
        comprobante.setSerie(documento.getSerie());
        comprobante.setNumero(String.format("%08d", Integer.parseInt(documento.getNumero())));
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
//        comprobante.setIncluirPdf(documento.getIncluir_pdf());
//        comprobante.setIncluirXml(documento.getIncluir_xml());
        comprobante.setSumatoriaIGV(SumatoriaIGV);
        comprobante.setSumatoriaISC(SumatoriaISC);
        comprobante.setSumatoriaOtrosCargos(BigDecimal.ZERO);
        comprobante.setSumatoriaOtrosTributos(BigDecimal.ZERO);
        comprobante.setTotalValorVentasGravadas(totalValorVentasGravadas);
        comprobante.setTotalValorVentasInafectas(totalValorVentasInafectas);
        comprobante.setTotalValorVentasExoneradas(totalValorVentasExoneradas);
        comprobante.setTotalValorVenta(totalValorVenta);
        comprobante.setCodLocal("0000");
        comprobante.setFormaPago(documento.getForma_pago());
        comprobante.setPorcentajeIGV(new BigDecimal(Constantes.PORCENTAJE_IGV));
        comprobante.setMontoNetoPendiente(documento.getMonto_neto_pendiente());
        comprobante.setTipoMonedaMontoNetoPendiente(documento.getMoneda_monto_neto_pendiente());
        comprobante.setDescuentosGlobales(BigDecimal.ZERO);
        if (comprobante.getTipo().equals("07") || comprobante.getTipo().equals("08")) {
            comprobante.setNotaMotivo(documento.getNota_motivo());
            comprobante.setNotaTipo(documento.getNota_tipo());
            comprobante.setNotaReferenciaTipo(documento.getNota_referencia_tipo());
            comprobante.setNotaReferenciaSerie(documento.getNota_referencia_serie());
            comprobante.setNotaReferenciaNumero(String.format("%08d", Integer.parseInt(documento.getNota_referencia_numero())));
        }

        List<FacturaElectronicaDet> list = new ArrayList<>();
        List<FacturaElectronicaTax> listTax = new ArrayList<>();
        List<FacturaElectronicaCuotas> listCuotas = new ArrayList<>();
        BigDecimal baseamt = BigDecimal.ZERO, taxtotal = BigDecimal.ZERO;

        for (ComprobanteDet comprobanteDet : documento.getLista_productos()) {
            FacturaElectronicaDet det = new FacturaElectronicaDet();
            det.setCodigo(comprobanteDet.getCodigo());
            det.setDescripcion(comprobanteDet.getDescripcion());
            det.setUnidadMedida(comprobanteDet.getUnidad_medida());
            det.setCantidad(comprobanteDet.getCantidad());
            det.setPrecioVentaUnitario(comprobanteDet.getPrecio_unitario());
            det.setValorVentaItem(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacion_igv()));
            det.setValorUnitario(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacion_igv()));
            det.setAfectacionIgv(comprobanteDet.getAfectacion_igv());
            det.setAfectacionIGVCode(comprobanteDet.getTipo_igv());
            det.setDescuento(comprobanteDet.getDescuento_porcentaje().
                    divide(new BigDecimal(100)).
                    multiply(comprobanteDet.getCantidad().multiply(comprobanteDet.getPrecio_unitario())));
            det.setRecargo(comprobanteDet.getRecargo());
            list.add(det);

            baseamt = baseamt.add(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacion_igv()));
            taxtotal = taxtotal.add(comprobanteDet.getAfectacion_igv());
            FacturaElectronicaTax facturaElectronicaTax = new FacturaElectronicaTax();
            facturaElectronicaTax.setTaxId(1000);
            facturaElectronicaTax.setBaseamt(baseamt);
            facturaElectronicaTax.setTaxtotal(taxtotal);
            listTax.add(facturaElectronicaTax);
        }

        if (!CollectionUtils.isEmpty(documento.getLista_cuotas())) {

//                BigDecimal sumaCoutas = BigDecimal.ZERO;
            for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {
                FacturaElectronicaCuotas det = new FacturaElectronicaCuotas();
                det.setMtoCuotaPago(detalle.getMonto_pago());
                det.setTipMonedaCuotaPago(detalle.getTipo_moneda_pago());
                det.setFecCuotaPago(new SimpleDateFormat("dd/MM/yyyy").parse(detalle.getFecha_pago()));
                listCuotas.add(det);
            }

        }

        comprobante.setListFacturaElectronicaDet(list);
        comprobante.setListFacturaElectronicaTax(listTax);
        comprobante.setListFacturaElectronicaCuotas(listCuotas);

        comprobante.getListFacturaElectronicaDet().stream().forEach(item -> {
            comprobante.addFacturaElectronicaDet(item);
        });
        comprobante.getListFacturaElectronicaTax().stream().forEach(item -> {
            comprobante.addFacturaElectronicaTax(item);
        });
        comprobante.getListFacturaElectronicaCuotas().stream().forEach(item -> {
            comprobante.addFacturaElectronicaCuotas(item);
        });
        //constantes o bd
        comprobante.setIndSituacion("01");
        return comprobante;
    }

    @Override
    public String validarComprobante(ComprobanteCab documento) {
        if (documento.getCliente_tipo().equals("1")
                && String.format("%02d", Integer.parseInt(documento.getTipo())).equals("01")) {
            return "El dato ingresado en el tipo de documento de identidad del receptor no esta permitido para el tipo de comprobante";
        }
        if (documento.getCliente_tipo().equals("1") && documento.getCliente_documento().length() != 8
                || documento.getCliente_tipo().equals("6") && documento.getCliente_documento().length() != 11) {
            return "El número de documento del cliente no cumple con el tamaño requerido para el tipo de comprobante";
        }
        if (CollectionUtils.isEmpty(documento.getLista_productos())) {
            return "Debe indicar el detalle de productos del comprobante, campo: lista_productos";
        }
        if (documento.getTipo().equals("01") || documento.getTipo().equals("03")) {
            if (!documento.getForma_pago().equals(Constantes.FORMA_PAGO_CONTADO) && !documento.getForma_pago().equals(Constantes.FORMA_PAGO_CREDITO)) {
                return "El campo forma de pago solo acepta los valores Contado/Credito";
            }
            if (documento.getForma_pago().equals(Constantes.FORMA_PAGO_CREDITO) && CollectionUtils.isEmpty(documento.getLista_cuotas())) {
                return "Si la forma de pago es Credito debe indicar al menos una cuota, campo: lista_cuotas";
            }
            if (documento.getForma_pago().equals(Constantes.FORMA_PAGO_CREDITO)
                    && FacturadorUtil.isNullOrEmpty(documento.getMonto_neto_pendiente())) {
                return "Si la forma de pago es Credito debe indicar el monto neto pendiente de pago";
            }
        }
//        boolean est = FacturadorUtil.isNullOrEmpty(documento.getNota_tipo());

        if ((documento.getTipo().equals("07") || documento.getTipo().equals("08"))
                && FacturadorUtil.isNullOrEmpty(documento.getNota_tipo())) {
            return "Para el tipo de documento debe indicar el código del motivo";
        }
        if ((documento.getTipo().equals("07") || documento.getTipo().equals("08"))
                && FacturadorUtil.isNullOrEmpty(documento.getNota_motivo())) {
            return "Para el tipo de documento debe indicar la descripción motivo";
        }
        if ((documento.getTipo().equals("07") || documento.getTipo().equals("08"))
                && FacturadorUtil.isNullOrEmpty(documento.getNota_referencia_tipo())) {
            return "Para el tipo de documento debe indicar el tipo de documento referenciado";
        }
        if ((documento.getTipo().equals("07") || documento.getTipo().equals("08"))
                && FacturadorUtil.isNullOrEmpty(documento.getNota_referencia_serie())) {
            return "Para el tipo de documento debe indicar la serie del documento referenciado";
        }
        if ((documento.getTipo().equals("07") || documento.getTipo().equals("08"))
                && FacturadorUtil.isNullOrEmpty(documento.getNota_referencia_numero())) {
            return "Para el tipo de documento debe indicar el número del documento referenciado";
        }
        for (ComprobanteDet item : documento.getLista_productos()) {
            if (!(item.getUnidad_medida().equals("NIU") || item.getUnidad_medida().equals("ZZ"))) {
                return "Código de unidad de medida no soportado";
            }
            if (Integer.valueOf(item.getTipo_igv()) >= 10 && Integer.valueOf(item.getTipo_igv()) <= 20
                    && item.getAfectacion_igv() == BigDecimal.ZERO) {
                return "El monto de afectación de IGV por linea debe ser diferente a 0.00.";
            }
        }
        //1. cambiar por clase catalogos
        //2. externalizar archivo
        List<String> listDocIds = Arrays.asList("0", "1", "4", "6", "7", "A");
        if (!listDocIds.contains(documento.getCliente_tipo())) {
            return "El tipo de documento de identidad no existe";
        }
        if (!(documento.getTipo().equals("07") || documento.getTipo().equals("08") || documento.getTipo().equals("03"))) {
            if (documento.getForma_pago().equals(Constantes.FORMA_PAGO_CONTADO) && !CollectionUtils.isEmpty(documento.getLista_cuotas())) {
                return "Si la forma de pago es Contado no es necesario indicar la lista de cuotas, campo: lista_cuotas";
            }
            for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {
                //                Date fechaPago = null;
                try {
                    Date fechaPago = new SimpleDateFormat("dd/MM/yyyy").parse(detalle.getFecha_pago());
                } catch (Exception e) {
                    return "Si la forma de pago es Credito la fecha de pago no debe estar vacía y debe tener formato correcto dd/MM/yyyy, campo: fecha_pago";
                }
                if (FacturadorUtil.isNullOrEmpty(detalle.getMonto_pago())) {
                    return "Si la forma de pago es Credito debe indicar al monto de la cuota, campo: monto_pago";
                }
                if (FacturadorUtil.isNullOrEmpty(detalle.getTipo_moneda_pago())) {
                    return "Si la forma de pago es Credito debe indicar el tipo de moneda de la cuota, campo: tipo_moneda_pago";
                }
            }
            if (!CollectionUtils.isEmpty(documento.getLista_cuotas())) {

                BigDecimal sumaCoutas = BigDecimal.ZERO;
                for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {

                    sumaCoutas = sumaCoutas.add(detalle.getMonto_pago());

                }
                if (sumaCoutas.compareTo(documento.getMonto_neto_pendiente()) != 0) {
                    return "La suma de las cuotas debe ser igual al Monto neto pendiente de pago";

                }
            }
        }
        String res = validarPlazo(documento);
        if (!res.equals("")) {
            return res;
        }
        return "";
    }

    private String validarPlazo(ComprobanteCab documento) {
//        String fecEmision = (String) expr.evaluate(document, XPathConstants.STRING);
        String dateString = documento.getFecha_emision().substring(6, 10)
                + documento.getFecha_emision().substring(3, 5)
                + documento.getFecha_emision().substring(0, 2);
        Integer fecEmisionInt = Integer.valueOf(dateString);
        log.debug("fecEmision: " + documento.getFecha_emision() + ", " + fecEmisionInt);
        String result = "";
        try {
            if (fecEmisionInt >= 20200107) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date fecEmisionDate = null;
                fecEmisionDate = df.parse(documento.getFecha_emision());
                Date today = new Date();
                long diff = today.getTime() - fecEmisionDate.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                Integer nroDias = Integer.valueOf(this.appConfig.getPlazoBoleta());
                if (days > nroDias) {
                    if (documento.getTipo().equals("03")) {
                        log.debug("El XML fue generado, pero el Comprobante tiene mas de " + nroDias + " dís. Emisión: " + documento.getFecha_emision() + ". Use el resumen diario para generar y enviar.");
                    }

                    if (documento.getTipo().equals("01")) {
                        result = "No se puede generar el XML, el Comprobante tiene mas de " + nroDias + " días. Emisión: " + documento.getFecha_emision();
                    }

                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.bean.MailBean;
import com.deinsoft.efacturador3.bean.ResumenDiarioBean;
import com.deinsoft.efacturador3.bean.ResumenDiarioBeanDet;
import com.deinsoft.efacturador3.bean.ResumenDiarioBeanTax;
import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.config.XsltCpePath;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.model.ResumenDiario;
import com.deinsoft.efacturador3.model.ResumenDiarioDet;
import com.deinsoft.efacturador3.model.ResumenDiarioTax;
import com.deinsoft.efacturador3.repository.ResumenDiarioRepository;
import com.deinsoft.efacturador3.service.ComunesService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.GenerarDocumentosService;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.service.ResumenDiarioService;
import com.deinsoft.efacturador3.soap.gencdp.ExceptionDetail;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import com.deinsoft.efacturador3.util.Catalogos;
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import com.deinsoft.efacturador3.util.Impresion;
import com.deinsoft.efacturador3.util.SendMail;
import com.deinsoft.efacturador3.validator.XsdCpeValidator;
import com.deinsoft.efacturador3.validator.XsltCpeValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author EDWARD-PC
 */
@Service
public class ResumenDiarioServiceImpl implements ResumenDiarioService {

    private static final Logger log = LoggerFactory.getLogger(ResumenDiarioServiceImpl.class);

    @Autowired
    private GenerarDocumentosService generarDocumentosService;

    @Autowired
    ResumenDiarioRepository resumenDiarioRepository;

    @Autowired
    FacturaElectronicaService facturaElectronicaService;

    @Autowired
    private ComunesService comunesService;

    @Autowired
    AppConfig appConfig;

    @Autowired
    private XsltCpePath xsltCpePath;

    @Override
    public ResumenDiario getResumenDiarioById(long id) {
        return resumenDiarioRepository.getById(id);
    }

    @Override
    public List<ResumenDiario> getResumenDiarios() {
        return resumenDiarioRepository.findAll(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResumenDiario save(ResumenDiario e) {
        return resumenDiarioRepository.save(e);
    }

    @Override
    public List<ResumenDiario> saveAll(List<ResumenDiario> e) {
        return resumenDiarioRepository.saveAll(e);
    }

    public ResumenDiario toResumenDiarioModel(ResumenDiarioBean resumenDiarioBean, Empresa empresa) throws ParseException {
        ResumenDiario cab = new ResumenDiario();
        List<ResumenDiario> list = new ArrayList<>();
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(resumenDiarioBean.getFechaEmision());
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(resumenDiarioBean.getFechaResumen());
        cab.setFechaEmision(date1);
        cab.setFechaResumen(date2);
        List<ResumenDiarioDet> listDet = new ArrayList<>();
        List<ResumenDiarioTax> listTax = new ArrayList<>();
        for (ResumenDiarioBeanDet resumenDiarioBeanDet : resumenDiarioBean.getLista_comprobantes()) {
            try {
                ResumenDiarioDet e = new ResumenDiarioDet();
                e.setTipDocResumen(resumenDiarioBeanDet.getTipDocResumen());
                e.setNroDocumento(resumenDiarioBeanDet.getNroDocumento());
                BeanUtils.copyProperties(e, resumenDiarioBeanDet);
                listDet.add(e);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(ResumenDiarioServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                java.util.logging.Logger.getLogger(ResumenDiarioServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (ResumenDiarioBeanTax resumenDiarioBeanTax : resumenDiarioBean.getLista_tributos()) {
            try {
                ResumenDiarioTax e = new ResumenDiarioTax();
                BeanUtils.copyProperties(e, resumenDiarioBeanTax);
                listTax.add(e);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(ResumenDiarioServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                java.util.logging.Logger.getLogger(ResumenDiarioServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        cab.setListResumenDiarioDet(listDet);
        cab.setListResumenDiarioTax(listTax);
        cab.setIndSituacion("01");
        cab.getListResumenDiarioDet().stream().forEach(item -> {
            cab.addResumenDiarioDet(item);
        });
        cab.getListResumenDiarioTax().stream().forEach(item -> {
            cab.addResumenDiarioTax(item);
        });
        int total = 1;
        List<ResumenDiario> listT = getResumenDiarios();
        if (listT != null) {
            total = listT.size() + 1;
        }
        String dateString = resumenDiarioBean.getFechaResumen().substring(6, 10)
                + resumenDiarioBean.getFechaResumen().substring(3, 5)
                + resumenDiarioBean.getFechaResumen().substring(0, 2);
        cab.setNombreArchivo(empresa.getNumdoc() + "-" + Constantes.CONSTANTE_TIPO_DOCUMENTO_RBOLETAS + "-" + dateString + "-" + total);
        return cab;
    }

    @Override
    @Transactional
    public Map<String, Object> generarComprobantePagoSunat(String rootpath, ResumenDiario resumenDiario) throws TransferirArchivoException {
        XsltCpeValidator xsltCpeValidator = new XsltCpeValidator(this.xsltCpePath);
        XsdCpeValidator xsdCpeValidator = new XsdCpeValidator(this.xsltCpePath);
        Map<String, Object> retorno = new HashMap<>();
        ResumenDiario resumenDiarioResult = null;
        long ticket = Calendar.getInstance().getTimeInMillis();
        retorno.put("ticketOperacion", ticket);
        try {
//            String retorno = "01";
            String nomFile = resumenDiario.getNombreArchivo();
            String tipo = Constantes.CONSTANTE_TIPO_DOCUMENTO_RBOLETAS;
            resumenDiarioResult = save(resumenDiario);
            if (Constantes.CONSTANTE_SITUACION_POR_GENERAR_XML.equals(resumenDiario.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_CON_ERRORES.equals(resumenDiario.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_XML_VALIDAR.equals(resumenDiario.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_ENVIADO_RECHAZADO.equals(resumenDiario.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_ENVIADO_ANULADO.equals(resumenDiario.getIndSituacion())) {
                log.debug("BandejaDocumentosServiceImpl.generarComprobantePagoSunat...tipoComprobante: " + tipo);

                this.generarDocumentosService.formatoPlantillaXmlResumenDiario(rootpath, resumenDiario);

                retorno.putAll(this.generarDocumentosService.firmarXml(rootpath, resumenDiario.getEmpresa(), nomFile));

                xsdCpeValidator.validarSchemaXML(rootpath, tipo, rootpath + "/" + resumenDiario.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml");

                xsltCpeValidator.validarXMLYComprimir(rootpath, resumenDiario.getEmpresa(), tipo, rootpath + "/" + resumenDiario.getEmpresa().getNumdoc() + "/PARSE/", nomFile);

                resumenDiario.setXmlHash(retorno.get("xmlHash").toString());
//                String tempDescripcionPluralMoneda = "SOLES";
//                ByteArrayInputStream stream = Impresion.Imprimir(rootpath + "TEMP/", 1, documento, tempDescripcionPluralMoneda);
//                int n = stream.available();
//                byte[] bytes = new byte[n];
//                stream.read(bytes, 0, n);

//                retorno.put("pdfBase64", bytes);
//                FileUtils.writeByteArrayToFile(new File(rootpath + "TEMP/" + nomFile + ".pdf"), bytes);
//                
//                if (!appConfig.getEnvironment().equals("PRODUCTION")) {
//                    FileUtils.writeByteArrayToFile(new File("D:/report.pdf"), bytes);
//                }
//                String[] adjuntos = {rootpath + "/" + resumenDiario.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml",
//                    rootpath + "TEMP/" + nomFile + ".pdf"};
                resumenDiarioResult.setFechaGenXml(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
                resumenDiarioResult.setIndSituacion(Constantes.CONSTANTE_SITUACION_XML_GENERADO);
                resumenDiarioResult.setTicketOperacion(ticket);
                save(resumenDiarioResult);

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
    @Transactional
    public Map<String, Object> generarComprobantePagoSunatFromFacturas(List<Long> listIds) throws TransferirArchivoException {
        XsltCpeValidator xsltCpeValidator = new XsltCpeValidator(this.xsltCpePath);
        XsdCpeValidator xsdCpeValidator = new XsdCpeValidator(this.xsltCpePath);
        Map<String, Object> retorno = new HashMap<>();
        ResumenDiario resumenDiarioResult = null;
        long ticket = Calendar.getInstance().getTimeInMillis();
        retorno.put("ticketOperacion", ticket);
        try {
            List<FacturaElectronica> list = facturaElectronicaService.getByIdIn(listIds);
            if (list == null || (list != null && list.size() == 0)) {
                return null;
            }
            Empresa empresa = list.get(0).getEmpresa();
            ResumenDiarioBean rd = new ResumenDiarioBean();
            rd.setFechaEmision(list.get(0).getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            rd.setFechaResumen(list.get(0).getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            List<ResumenDiarioBeanDet> listDet = new ArrayList<>();
            List<ResumenDiarioBeanTax> listTax = new ArrayList<>();
            int cont = 0;
            LocalDate fechaAnterior = null;
            for (FacturaElectronica facturaElectronica : list) {
                if (fechaAnterior != null && fechaAnterior.compareTo(facturaElectronica.getFechaEmision()) != 0) {
                    retorno.clear();
                    retorno.put("code", "003");
                    retorno.put("message", "Los comprobantes seleccionados tienen diferente fecha de emisión");
                    return retorno;
                }
                if (!(Constantes.CONSTANTE_SITUACION_XML_GENERADO.equals(facturaElectronica.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_ENVIADO_RECHAZADO.equals(facturaElectronica.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_CON_ERRORES.equals(facturaElectronica.getIndSituacion()))) {
                    retorno.clear();
                    retorno.put("code", "003");
                    retorno.put("message", "El comprobante " + facturaElectronica.getSerie() + "-" + facturaElectronica.getNumero() + " se encuentra en una situaciòn incorrecta o ya fue enviado");
                    return retorno;
                }
                ResumenDiarioBeanDet det = new ResumenDiarioBeanDet();
                det.setTipDocResumen(facturaElectronica.getTipo());
                det.setNroDocumento(facturaElectronica.getSerie() + "-" + facturaElectronica.getNumero());
                det.setTipDocUsuario(facturaElectronica.getClienteTipo());
                det.setNumDocUsuario(facturaElectronica.getClienteDocumento());
                det.setMoneda(facturaElectronica.getMoneda());
                det.setTotValGrabado(Impresion.df.format(facturaElectronica.getTotalValorVentasGravadas()));
                det.setTotValExonerado(Impresion.df.format(facturaElectronica.getTotalValorVentasExoneradas()));
                det.setTotValInafecto(Impresion.df.format(facturaElectronica.getTotalValorVentasInafectas()));
                det.setTotValExportado("0.00");
                det.setTotValGratuito("0.00");
                det.setTotOtroCargo("0.00");
                det.setTotImpCpe(Impresion.df.format(facturaElectronica.getTotalValorVenta()));
                det.setTipDocModifico(facturaElectronica.getNotaReferenciaTipo() == null ? "" : facturaElectronica.getNotaReferenciaTipo());
                det.setSerDocModifico(facturaElectronica.getNotaReferenciaSerie() == null ? "" : facturaElectronica.getNotaReferenciaSerie());
                det.setNumDocModifico(facturaElectronica.getNotaReferenciaNumero() == null ? "" : facturaElectronica.getNotaReferenciaNumero());
                det.setCondicion("1");
                listDet.add(det);

                cont++;
                for (FacturaElectronicaTax facturaElectronicaTax : facturaElectronica.getListFacturaElectronicaTax()) {
                    ResumenDiarioBeanTax tax = new ResumenDiarioBeanTax();

                    tax.setIdLineaRd(String.valueOf(cont));
                    tax.setIdeTributoRd(facturaElectronicaTax.getTaxId());
                    tax.setCodTipTributoRd(facturaElectronicaTax.getCodTipTributo());
                    tax.setNomTributoRd(facturaElectronicaTax.getNomTributo());
                    tax.setMtoBaseImponibleRd(Impresion.df.format(facturaElectronicaTax.getMtoBaseImponible()));
                    tax.setMtoTributoRd(Impresion.df.format(facturaElectronicaTax.getMtoTributo()));
                    listTax.add(tax);
                }
                fechaAnterior = facturaElectronica.getFechaEmision();
            }
//            listTax = listTax.stream()
//                    .collect(Collectors.groupingBy(foo -> foo.getIdeTributoRd()))
//                    .entrySet().stream()
//                    .map(e -> e.getValue().stream()
//                    .reduce((f1, f2) -> {
//                        int cont2 = 0;
//                        return new ResumenDiarioBeanTax(String.valueOf(++cont2), f1.getIdeTributoRd(), f1.getNomTributoRd(),
//                                f1.getCodTipTributoRd(),
//                                Impresion.df.format(Float.parseFloat(f1.getMtoBaseImponibleRd()) + Float.parseFloat(f2.getMtoBaseImponibleRd())),
//                                Impresion.df.format(Float.parseFloat(f1.getMtoTributoRd()) + Float.parseFloat(f2.getMtoTributoRd())));
//                    }))
//                    .map(f -> f.get())
//                    .collect(Collectors.toList());

            rd.setLista_comprobantes(listDet);
            rd.setLista_tributos(listTax);
            ResumenDiario resumenDiario = toResumenDiarioModel(rd, empresa);
            resumenDiario.setEmpresa(empresa);
            String nomFile = resumenDiario.getNombreArchivo();
            String tipo = Constantes.CONSTANTE_TIPO_DOCUMENTO_RBOLETAS;
            resumenDiarioResult = save(resumenDiario);
            if (Constantes.CONSTANTE_SITUACION_POR_GENERAR_XML.equals(resumenDiario.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_CON_ERRORES.equals(resumenDiario.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_XML_VALIDAR.equals(resumenDiario.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_ENVIADO_RECHAZADO.equals(resumenDiario.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_ENVIADO_ANULADO.equals(resumenDiario.getIndSituacion())) {
                log.debug("BandejaDocumentosServiceImpl.generarComprobantePagoSunat...tipoComprobante: " + tipo);

                this.generarDocumentosService.formatoPlantillaXmlResumenDiario(appConfig.getRootPath(), resumenDiario);

                retorno.putAll(this.generarDocumentosService.firmarXml(appConfig.getRootPath(), resumenDiario.getEmpresa(), nomFile));

                xsdCpeValidator.validarSchemaXML(appConfig.getRootPath(), tipo, appConfig.getRootPath() + "/" + resumenDiario.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml");

                xsltCpeValidator.validarXMLYComprimir(appConfig.getRootPath(), resumenDiario.getEmpresa(), tipo, appConfig.getRootPath() + "/" + resumenDiario.getEmpresa().getNumdoc() + "/PARSE/", nomFile);

                resumenDiario.setXmlHash(retorno.get("xmlHash").toString());
                resumenDiarioResult.setFechaGenXml(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
                resumenDiarioResult.setIndSituacion(Constantes.CONSTANTE_SITUACION_XML_GENERADO);
                resumenDiarioResult.setTicketOperacion(ticket);
                save(resumenDiarioResult);

                retorno = sendSUNAT(resumenDiarioResult.getId());
            }
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
            retorno.clear();
            retorno.put("code", "003");
            retorno.put("message", e.getMessage());
            return retorno;
//            throw new TransferirArchivoException(e.getMessage(), exceptionDetail);
        }
    }

    @Override
    public Map<String, Object> sendSUNAT(Long id) {
        log.debug("FacturaController.enviarXML...Enviar Comprobante");
        HashMap<String, Object> retorno = new HashMap<>();
        ResumenDiario resumenDiario = null;
        String mensajeValidacion = "", resultadoProceso = "EXITO";
        try {
            resumenDiario = getResumenDiarioById(id);
            if (resumenDiario == null || (resumenDiario != null && resumenDiario.getNombreArchivo() == null)) {
                retorno.put("code", "003");
                retorno.put("message", "El comprobante ya se encuentra en estado ENVIADO y ACEPTADO");
                return retorno;
            }
            if ("02".equals(resumenDiario.getIndSituacion())
                    || "10".equals(resumenDiario.getIndSituacion())
                    || "06".equals(resumenDiario.getIndSituacion())) {
                String urlWebService = (appConfig.getUrlServiceCDP() != null) ? appConfig.getUrlServiceCDP() : "XX";
                String tipoComprobante = "RA";
                log.debug("FacturaController.enviarXML...Validando Conexión a Internet");
                String[] rutaUrl = urlWebService.split("\\/");
                log.debug("FacturaController.enviarXML...tokens: " + rutaUrl[2]);
                this.comunesService.validarConexion(rutaUrl[2], 443);

                log.debug("FacturaController.enviarXML...Enviando Documento");
                log.debug("FacturaController.enviarXML...urlWebService: " + urlWebService);
                log.debug("FacturaController.enviarXML...filename: " + resumenDiario.getNombreArchivo());
                log.debug("FacturaController.enviarXML...tipoComprobante: " + tipoComprobante);
                BillServiceModel res = this.comunesService.enviarArchivoSunat(urlWebService, appConfig.getRootPath(),
                        resumenDiario.getNombreArchivo(), resumenDiario.getEmpresa());
                resumenDiario.setFechaEnvio(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
                resumenDiario.setIndSituacion(res.getCode() == null ? Constantes.CONSTANTE_SITUACION_ENVIADO_PROCESANDO
                        : res.getCode().toString().equals("0") ? Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO
                        : Constantes.CONSTANTE_SITUACION_CON_ERRORES);
                resumenDiario.setObservacionEnvio(res.getDescription());
                resumenDiario.setTicketSunat(res.getTicket());
                ResumenDiario ResumenDiarioResult = save(resumenDiario);

                if (res.getCode() == null && res.getTicket() != null) {
                    Thread.sleep(5000);
                    res = comunesService.consultarTicketSUNAT(urlWebService, appConfig.getRootPath(), res.getTicket(), ResumenDiarioResult.getEmpresa());
                    if(res == null){
                        ResumenDiarioResult.setIndSituacion(Constantes.CONSTANTE_SITUACION_CON_ERRORES);
                        ResumenDiarioResult.setObservacionEnvio("Ticket con errores - Internal Server Error");
                        save(ResumenDiarioResult);
                    }else{
                        if (res.getCode() == 0) {
                            for (Iterator<ResumenDiarioDet> it = ResumenDiarioResult.getListResumenDiarioDet().iterator(); it.hasNext();) {
                                ResumenDiarioDet resumenDiarioDet = it.next();
                                FacturaElectronica fact = new FacturaElectronica();
                                fact.setSerie(resumenDiarioDet.getNroDocumento().split("-")[0]);
                                fact.setNumero(String.format("%08d", Integer.valueOf(resumenDiarioDet.getNroDocumento().split("-")[1])));
                                fact.setEmpresa(ResumenDiarioResult.getEmpresa());
                                List<FacturaElectronica> listFact = facturaElectronicaService.getBySerieAndNumeroAndEmpresaId(fact);
                                fact = listFact.get(0);
                                fact.setFechaEnvio(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
                                fact.setIndSituacion(res.getCode().toString().equals("0") ? Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO : Constantes.CONSTANTE_SITUACION_CON_ERRORES);
                                fact.setObservacionEnvio(res.getDescription());
                                fact.setTicketSunat(res.getTicket());
                                facturaElectronicaService.save(fact);

                            }
                            ResumenDiarioResult.setIndSituacion(res.getCode() == null ? Constantes.CONSTANTE_SITUACION_ENVIADO_PROCESANDO
                                    : res.getCode().toString().equals("0") ? Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO
                                    : Constantes.CONSTANTE_SITUACION_CON_ERRORES);
                            ResumenDiarioResult.setObservacionEnvio(res.getDescription());
                            save(ResumenDiarioResult);
                        }else if (res.getCode() == 98) {
                            ResumenDiarioResult.setIndSituacion(Constantes.CONSTANTE_SITUACION_ENVIADO_PROCESANDO);
                            ResumenDiarioResult.setObservacionEnvio("En espera: "+ res.getCode());
                            save(ResumenDiarioResult);
                        }
                    }
                    

                }
                Map<String, Object> map = new ObjectMapper().convertValue(res, Map.class);
                return map;

            } else {
                mensajeValidacion = "El documento se encuentra en una situación incrrecta o ya fue enviado";
                resultadoProceso = "-1";
            }
        } catch (Exception e) {
            String mensaje = "Hubo un problema al invocar servicio SUNAT: " + e.getMessage();
            e.printStackTrace();
            log.error(mensaje);
            if (resumenDiario != null) {
                resumenDiario.setFechaEnvio(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
                resumenDiario.setIndSituacion("06");
                resumenDiario.setObservacionEnvio(mensaje);
                save(resumenDiario);
            }
            resultadoProceso = "003";
            mensajeValidacion = e.getMessage();
            return retorno;
        }
        retorno.put("message", mensajeValidacion);
        retorno.put("code", resultadoProceso);
        log.debug("SoftwareFacturadorController.enviarXML...Terminando el procesamiento");
        return retorno;
    }
}

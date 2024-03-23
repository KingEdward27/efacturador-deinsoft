/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ComprobanteCuotas;
import com.deinsoft.efacturador3.bean.ComprobanteDet;
import com.deinsoft.efacturador3.bean.ComprobanteTax;
import com.deinsoft.efacturador3.bean.MailBean;
import com.deinsoft.efacturador3.bean.ParamBean;
import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.config.XsltCpePath;
import com.deinsoft.efacturador3.exception.XsdException;
import com.deinsoft.efacturador3.exception.XsltException;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaCuotas;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.FacturaElectronicaLeyenda;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.model.ResumenDiarioDet;
import com.deinsoft.efacturador3.repository.ErrorRepository;
import com.deinsoft.efacturador3.repository.FacturaElectronicaRepository;
import com.deinsoft.efacturador3.service.ComunesService;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.GenerarDocumentosService;
import com.deinsoft.efacturador3.soap.gencdp.ExceptionDetail;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import com.deinsoft.efacturador3.util.Catalogos;
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import com.deinsoft.efacturador3.util.Impresion;
import com.deinsoft.efacturador3.util.SendMail;
import com.deinsoft.efacturador3.validationapirest.ValidacionRespuestaApi;
import com.deinsoft.efacturador3.validationapirest.ValidacionSUNAT;
import com.deinsoft.efacturador3.validationapirest.ValidationBody;
import com.deinsoft.efacturador3.validator.XsdCpeValidator;
import com.deinsoft.efacturador3.validator.XsltCpeValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.project.openubl.xmlsenderws.webservices.managers.BillServiceManager;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.wrappers.ServiceConfig;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author EDWARD-PC
 */
@Service
public class FacturaElectronicaServiceImpl implements FacturaElectronicaService {

    private static final Logger log = LoggerFactory.getLogger(FacturaElectronicaServiceImpl.class);

    @Autowired
    FacturaElectronicaRepository facturaElectronicaRepository;

    @Autowired
    private ComunesService comunesService;

    @Autowired
    private GenerarDocumentosService generarDocumentosService;

//    @Autowired
//    private ErrorRepository errorDao;
    @Autowired
    private XsltCpePath xsltCpePath;

    @Autowired
    AppConfig appConfig;

    @Autowired
    private EmpresaService empresaService;

    @Override
    public FacturaElectronica getById(long id) {
        return facturaElectronicaRepository.getById(id);
    }

    @Override
    public FacturaElectronica findById(long id) {
        Optional<FacturaElectronica> fact = facturaElectronicaRepository.findById(id);
        if (fact.isPresent()) {
            return fact.get();
        }
        return null;
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
    public List<FacturaElectronica> getBySerieAndNumeroAndEmpresaId(FacturaElectronica facturaElectronica) {
        return facturaElectronicaRepository.findBySerieAndNumeroAndEmpresaId(facturaElectronica.getSerie(), facturaElectronica.getNumero(), facturaElectronica.getEmpresa().getId());
    }

    @Override
    public List<FacturaElectronica> getByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(FacturaElectronica facturaElectronica) {
        return facturaElectronicaRepository.findByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(facturaElectronica);
    }
    @Override
    public List<FacturaElectronica> getReportActComprobante(ParamBean paramBean) {
        return facturaElectronicaRepository.getReportActComprobante(paramBean);
    }
    @Override
    @Transactional
    public Map<String, Object> generarComprobantePagoSunat(long comprobanteId) throws TransferirArchivoException {
        log.debug("FacturaController.generarComprobantePagoSunat...inicio/params: " + String.valueOf(comprobanteId));
        Map<String, Object> retorno = new HashMap<>();
        FacturaElectronica facturaElectronicaResult = null;
        long ticket = Calendar.getInstance().getTimeInMillis();

        try {
//            String retorno = "01";
//            String tipoComprobante = null;
//            String nomFile = "";

            facturaElectronicaResult = getById(comprobanteId);
            if (facturaElectronicaResult.getIndSituacion().equals(Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO)) {
                retorno.put("code", "-2");
                retorno.put("message", "El comprobante ya se encuentra en estado ENVIADO y ACEPTADO");
                return retorno;
            }
            retorno.put("ticketOperacion", ticket);
            retorno.putAll(genXmlAndSignAndValidate(appConfig.getRootPath(), facturaElectronicaResult, ticket));
            return retorno;
        } catch (Exception e) {
            log.info(e.getMessage());
            retorno = new HashMap<>();
            retorno.put("code", "9999");
            retorno.put("message", e.getMessage());
            e.printStackTrace();
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setMessage(e.getMessage());
            throw new TransferirArchivoException(e.getMessage(), exceptionDetail);
        }
    }

    FacturaElectronica setDetailsReferencedLists(FacturaElectronica facturaElectronicaResult, FacturaElectronica f) throws IllegalAccessException, InvocationTargetException {
        try {
            List<FacturaElectronicaDet> list = facturaElectronicaResult.getListFacturaElectronicaDet().stream().map(mapper -> {
                FacturaElectronicaDet temp = new FacturaElectronicaDet();
                try {
                    BeanUtils.copyProperties(temp, mapper);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                temp.setId(null);
                temp.setFacturaElectronica(f);
                return temp;
            }).collect(Collectors.toList());

            List<FacturaElectronicaCuotas> listCuotas = facturaElectronicaResult.getListFacturaElectronicaCuotas().stream().map(mapper -> {
                FacturaElectronicaCuotas temp = new FacturaElectronicaCuotas();
                try {
                    BeanUtils.copyProperties(temp, mapper);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                temp.setId(null);
                temp.setFacturaElectronica(f);
                return temp;
            }).collect(Collectors.toList());

            List<FacturaElectronicaTax> listTax = facturaElectronicaResult.getListFacturaElectronicaTax().stream().map(mapper -> {
                FacturaElectronicaTax temp = new FacturaElectronicaTax();
                try {
                    BeanUtils.copyProperties(temp, mapper);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                temp.setId(null);
                temp.setFacturaElectronica(f);
                return temp;
            }).collect(Collectors.toList());

            List<FacturaElectronicaLeyenda> listLeyendas = facturaElectronicaResult.getListFacturaElectronicaLeyendas().stream().map(mapper -> {
                FacturaElectronicaLeyenda temp = new FacturaElectronicaLeyenda();
                try {
                    BeanUtils.copyProperties(temp, mapper);
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                }
                temp.setId(null);
                temp.setFacturaElectronica(f);
                return temp;
            }).collect(Collectors.toList());

            f.setListFacturaElectronicaDet(list);
            f.setListFacturaElectronicaCuotas(listCuotas);
            f.setListFacturaElectronicaTax(listTax);
            f.setListFacturaElectronicaLeyendas(listLeyendas);

            f.getListFacturaElectronicaDet().stream().forEach(item -> {
                f.addFacturaElectronicaDet(item);
            });
            f.getListFacturaElectronicaCuotas().stream().forEach(item -> {
                f.addFacturaElectronicaCuotas(item);
            });
            f.getListFacturaElectronicaTax().stream().forEach(item -> {
                f.addFacturaElectronicaTax(item);
            });
            f.getListFacturaElectronicaLeyendas().stream().forEach(item -> {
                f.addFacturaElectronicaLeyenda(item);
            });
        } catch (Exception e) {
        }

        return f;
    }

    @Override
    @Transactional
    public Map<String, Object> generarNotaCredito(long comprobanteId) throws TransferirArchivoException {
        log.debug("FacturaController.generarComprobantePagoSunat...inicio/params: " + String.valueOf(comprobanteId));
        Map<String, Object> retorno = new HashMap<>();
        FacturaElectronica facturaElectronicaResult = null;
        long ticket = Calendar.getInstance().getTimeInMillis();

        try {
            facturaElectronicaResult = findById(comprobanteId);
//            for (FacturaElectronicaDet facturaElectronicaDet : facturaElectronicaResult.getListFacturaElectronicaDet()) {
//                log.debug(facturaElectronicaDet.toString());
//            }
            FacturaElectronica f = new FacturaElectronica();
//            f = facturaElectronicaResult;
            BeanUtils.copyProperties(f, facturaElectronicaResult);
            f.setId(null);
            f.setTipo("07");
            f.setSerie("FN01");
            f.setNumero("00000011");
            f.setNotaReferenciaTipo(facturaElectronicaResult.getTipo());
            f.setNotaReferenciaSerie(facturaElectronicaResult.getSerie());
            f.setNotaReferenciaNumero(facturaElectronicaResult.getNumero());
            f.setNotaTipo("01");
            f.setNotaMotivo("Anulación de la operación");
            f.setIndSituacion("02");
            f.setObservacionEnvio(null);
//            f.setListFacturaElectronicaDet(setsda);
//            for (FacturaElectronicaDet facturaElectronicaDet : f.getListFacturaElectronicaDet()) {
//                f.addFacturaElectronicaDet(facturaElectronicaDet);
//            }
            FacturaElectronica f0 = setDetailsReferencedLists(facturaElectronicaResult, f);
            FacturaElectronica f2 = save(f0);

            List<FacturaElectronicaDet> list = f2.getListFacturaElectronicaDet().stream().map(mapper -> {
                mapper.setFacturaElectronica(f2);
                return mapper;
            }).collect(Collectors.toList());

            List<FacturaElectronicaCuotas> listCuotas = f2.getListFacturaElectronicaCuotas()
                    .stream().map(mapper -> {
                        mapper.setFacturaElectronica(f2);
                        return mapper;
                    }).collect(Collectors.toList());

            List<FacturaElectronicaTax> listTax = f2.getListFacturaElectronicaTax().stream().map(mapper -> {
                mapper.setFacturaElectronica(f2);
                return mapper;
            }).collect(Collectors.toList());

            List<FacturaElectronicaLeyenda> listLeyendas = f2.getListFacturaElectronicaLeyendas().stream().map(mapper -> {
                mapper.setFacturaElectronica(f2);
                return mapper;
            }).collect(Collectors.toList());

            f2.setListFacturaElectronicaDet(list);
            f2.setListFacturaElectronicaCuotas(listCuotas);
            f2.setListFacturaElectronicaTax(listTax);
            f2.setListFacturaElectronicaLeyendas(listLeyendas);
//            FacturaElectronica facturaElectronicaResult2 = getById(f.getId());
            if (f.getIndSituacion().equals(Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO)) {
                retorno.put("code", "003");
                retorno.put("message", "El comprobante ya se encuentra en estado ENVIADO y ACEPTADO");
                return retorno;
            }
            retorno.put("ticketOperacion", ticket);
            retorno.putAll(genXmlAndSignAndValidate(appConfig.getRootPath(), f2, ticket));
            return retorno;
        } catch (Exception e) {
            log.info(e.getMessage());
            retorno = new HashMap<>();
            retorno.put("code", "003");
            retorno.put("message", e.getMessage());
            e.printStackTrace();
            ExceptionDetail exceptionDetail = new ExceptionDetail();
            exceptionDetail.setMessage(e.getMessage());
            throw new TransferirArchivoException(e.getMessage(), exceptionDetail);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> generarComprobantePagoSunat(String rootpath, FacturaElectronica documento) throws TransferirArchivoException {
        log.debug("FacturaController.generarComprobantePagoSunat...inicio/params: " + documento.toString());
        Map<String, Object> retorno = new HashMap<>();
        FacturaElectronica facturaElectronicaResult = null;
        long ticket = Calendar.getInstance().getTimeInMillis();
        retorno.put("ticketOperacion", ticket);
        try {
//            String retorno = "01";
//            String tipoComprobante = null;
//            String nomFile = "";

            facturaElectronicaResult = save(documento);
            retorno.putAll(genXmlAndSignAndValidate(rootpath, facturaElectronicaResult, ticket));
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
    public Map<String, Object> sendToSUNAT(long comprobanteId) {
        log.debug("FacturaController.enviarXML...Consultar Comprobante");
        FacturaElectronica facturaElectronica = getById(comprobanteId);
        HashMap<String, Object> resultado = new HashMap<>();
        String mensajeValidacion = "", resultadoProceso = "EXITO";
        try {
            log.debug("FacturaController.enviarXML...Enviar Comprobante");

            if (Constantes.CONSTANTE_SITUACION_XML_GENERADO.equals(facturaElectronica.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_ENVIADO_RECHAZADO.equals(facturaElectronica.getIndSituacion())
                    || Constantes.CONSTANTE_SITUACION_CON_ERRORES.equals(facturaElectronica.getIndSituacion())) {

                String urlWebService = (appConfig.getUrlServiceCDP() != null) ? appConfig.getUrlServiceCDP() : "XX";
                String tipoComprobante = facturaElectronica.getTipo();
                String filename = facturaElectronica.getEmpresa().getNumdoc()
                        + "-" + String.format("%02d", Integer.parseInt(facturaElectronica.getTipo()))
                        + "-" + facturaElectronica.getSerie()
                        + "-" + String.format("%08d", Integer.parseInt(facturaElectronica.getNumero()));
                log.debug("FacturaController.enviarXML...Validando Conexión a Internet");
                String[] rutaUrl = urlWebService.split("\\/");
                log.debug("FacturaController.enviarXML...tokens: " + rutaUrl[2]);
                this.comunesService.validarConexion(rutaUrl[2], 443);

                log.debug("FacturaController.enviarXML...Enviando Documento");
                log.debug("FacturaController.enviarXML...urlWebService: " + urlWebService);
                log.debug("FacturaController.enviarXML...filename: " + filename);
                log.debug("FacturaController.enviarXML...tipoComprobante: " + tipoComprobante);
                BillServiceModel res = this.comunesService.enviarArchivoSunat(urlWebService, appConfig.getRootPath(), filename, facturaElectronica.getEmpresa());
                
                facturaElectronica.setFechaEnvio(LocalDateTime.now().plusHours(appConfig.getDiferenceHours())); 
                facturaElectronica.setIndSituacion(res.getCode().toString().equals("0") ? Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO : Constantes.CONSTANTE_SITUACION_CON_ERRORES);
                facturaElectronica.setObservacionEnvio(res.getDescription());
                facturaElectronica.setTicketSunat(res.getTicket());
                save(facturaElectronica);
                Map<String, Object> map = new ObjectMapper().convertValue(res, Map.class);
                return map;
//                retorno.put("resultadoWebService", resultadoWebService);
            } else {
                mensajeValidacion = "El documento se encuentra en una situación incrrecta o ya fue enviado";
                resultadoProceso = "-2";
            }

            log.debug("FacturaController.enviarXML...enviarComprobantePagoSunat Final");

        } catch (Exception e) {
            mensajeValidacion = "Hubo un problema al invocar servicio SUNAT: " + e.getMessage();
            e.printStackTrace();
            resultadoProceso = "9999";
            facturaElectronica.setFechaEnvio(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
            facturaElectronica.setIndSituacion("06");
            facturaElectronica.setObservacionEnvio(mensajeValidacion);
            save(facturaElectronica);
        }

        resultado.put("message", mensajeValidacion);
        resultado.put("code", resultadoProceso);
        log.debug("SoftwareFacturadorController.enviarXML...Terminando el procesamiento");
        return resultado;

    }

    @Override
    public void sendToSUNAT() {
        List<String> listSituacion = new ArrayList<>();
        listSituacion.add(Constantes.CONSTANTE_SITUACION_XML_GENERADO);
//        listSituacion.add(Constantes.CONSTANTE_SITUACION_ENVIADO_RECHAZADO);
//        listSituacion.add(Constantes.CONSTANTE_SITUACION_CON_ERRORES);
        String urlWebService = (appConfig.getUrlServiceCDP() != null) ? appConfig.getUrlServiceCDP() : "XX";
        int cont = 1;
        boolean connection = false;
        while (cont <= 3) {
            log.debug("FacturaElectronicaServiceImpl.sendToSUNAT...Validando Conexión a Internet");
            String[] rutaUrl = urlWebService.split("\\/");
            log.debug("FacturaElectronicaServiceImpl.sendToSUNAT...tokens: " + rutaUrl[2]);
            if (this.comunesService.validarConexion(rutaUrl[2], 443)) {
                connection = true;
                break;
            }
            cont++;
        }
        if (!connection) {
            log.debug("sunat connection down");
            return;
        }

        for (Empresa empresa : empresaService.getEmpresas()) {
            List<FacturaElectronica> list = facturaElectronicaRepository.
                    findToSendSunat(
                            empresa.getId(), Arrays.asList("01", "03"), listSituacion, "1", LocalDate.now().plusDays(-3));
            log.info("A enviar: " + String.valueOf(list.size()));

            list.forEach((facturaElectronica) -> {
                sendCp(facturaElectronica, urlWebService);

            });

        }
        log.debug("FacturaElectronicaServiceImpl.sendToSUNAT...enviarComprobantePagoSunat Final");
    }

    private void sendCp(FacturaElectronica facturaElectronica, String urlWebService) {
        try {

            String filename = facturaElectronica.getEmpresa().getNumdoc()
                    + "-" + String.format("%02d", Integer.parseInt(facturaElectronica.getTipo()))
                    + "-" + facturaElectronica.getSerie()
                    + "-" + String.format("%08d", Integer.parseInt(facturaElectronica.getNumero()));
//            log.debug("FacturaElectronicaServiceImpl.sendToSUNAT...filename: " + filename);
            //resultadoWebService = this.generarDocumentosService.enviarArchivoSunat(urlWebService, appConfig.getRootPath(), filename, facturaElectronica);
            BillServiceModel res = comunesService.enviarArchivoSunat(urlWebService, appConfig.getRootPath(), filename, facturaElectronica.getEmpresa());
//            log.debug("FacturaElectronicaServiceImpl.sendToSUNAT...res.getDescription(): " + res.getCode());
//            log.debug("FacturaElectronicaServiceImpl.sendToSUNAT...res.getDescription(): " + res.getDescription());
//            log.debug("FacturaElectronicaServiceImpl.sendToSUNAT...res.getDescription(): " + res.getTicket());
            facturaElectronica.setFechaEnvio(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
            facturaElectronica.setIndSituacion(res.getCode().toString().equals("0") ? Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO : Constantes.CONSTANTE_SITUACION_CON_ERRORES);
            facturaElectronica.setObservacionEnvio(res.getDescription());
            facturaElectronica.setTicketSunat(res.getTicket());
            save(facturaElectronica);
            log.info(res.getDescription());
            Thread.sleep(1000);
        } catch (Exception e) {
            String mensaje = "Hubo un problema al invocar servicio SUNAT: " + e.getMessage();
            e.printStackTrace();
            log.error(mensaje);
            facturaElectronica.setFechaEnvio(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
            facturaElectronica.setIndSituacion(Constantes.CONSTANTE_SITUACION_CON_ERRORES);
            facturaElectronica.setObservacionEnvio(mensaje);
            facturaElectronica.setNroIntentoEnvio(facturaElectronica.getNroIntentoEnvio() + 1);
            save(facturaElectronica);
        }
    }

    @Override
    public FacturaElectronica toFacturaModel(ComprobanteCab documento, Empresa e) throws TransferirArchivoException, ParseException {
        BigDecimal totalValorVentasGravadas = BigDecimal.ZERO, totalValorVentasInafectas = BigDecimal.ZERO,
                totalValorVentasExoneradas = BigDecimal.ZERO,
                SumatoriaIGV = BigDecimal.ZERO, SumatoriaISC = BigDecimal.ZERO,
                sumatoriaOtrosTributos = BigDecimal.ZERO, sumatoriaOtrosCargos = BigDecimal.ZERO, totalValorVenta = BigDecimal.ZERO;
        FacturaElectronica comprobante = new FacturaElectronica();
        //constantes o bd
        for (ComprobanteDet detalle : documento.getLista_productos()) {
            if (Integer.valueOf(detalle.getTipo_igv()) >= 10 && Integer.valueOf(detalle.getTipo_igv()) <= 20) {
                totalValorVentasGravadas = totalValorVentasGravadas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
                System.out.println("totalValorVentasGravadas: " + totalValorVentasGravadas.toString());
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
        System.out.println("totalValorVentasGravadas: " + totalValorVentasGravadas.toString());
        System.out.println("totalValorVentasGravadas: " + totalValorVentasGravadas.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        documento.setTotalValorVentasGravadas(totalValorVentasGravadas.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        documento.setTotalValorVentasInafectas(totalValorVentasInafectas.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        documento.setTotalValorVentasExoneradas(totalValorVentasExoneradas.setScale(2, BigDecimal.ROUND_HALF_EVEN));

        comprobante.setTipo(documento.getTipo());
        comprobante.setTipoOperacion(documento.getTipo_operacion());
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(documento.getFecha_emision());
        //	java.sql.Date dateSql = new java.sql.Date(date1.getTime());
        comprobante.setFechaEmision(LocalDate.parse(documento.getFecha_emision(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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
        comprobante.setTotalValorVenta(totalValorVenta.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        comprobante.setCodLocal("0000");
        comprobante.setFormaPago(documento.getForma_pago());
        comprobante.setPorcentajeIGV(new BigDecimal(Constantes.PORCENTAJE_IGV));
        comprobante.setMontoNetoPendiente(documento.getMonto_neto_pendiente() == null ? BigDecimal.ZERO : documento.getMonto_neto_pendiente());
        comprobante.setTipoMonedaMontoNetoPendiente(documento.getMoneda_monto_neto_pendiente());
        comprobante.setDescuentosGlobales(BigDecimal.ZERO);
        comprobante.setEstado("1");

        if (documento.getSerie_ref() != null && documento.getSerie_ref().length() == 4) {
            comprobante.setDocrefSerie(documento.getSerie_ref());
            comprobante.setDocrefNumero(String.format("%08d", Integer.parseInt(documento.getNumero_ref())));
            comprobante.setDocrefMonto(new BigDecimal(documento.getMonto_ref()));
            comprobante.setTotalValorVenta(totalValorVenta.subtract(comprobante.getDocrefMonto()));
            comprobante.setEmpresa(e);
            List<FacturaElectronica> comprobanteRel = facturaElectronicaRepository.findByDocrefSerieAndDocrefNumero(comprobante);
//            comprobante.setSumatoriaIGV(SumatoriaIGV.add(comprobanteRel.get(0).getSumatoriaIGV()));
        }
        if (documento.getFecha_ref() != null && !documento.getFecha_ref().equals("")) {
            comprobante.setDocrefFecha(LocalDate.parse(documento.getFecha_ref(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (comprobante.getTipo().equals("07") || comprobante.getTipo().equals("08")) {
            comprobante.setNotaMotivo(documento.getNota_motivo());
            comprobante.setNotaTipo(documento.getNota_tipo());
            comprobante.setNotaReferenciaTipo(documento.getNota_referencia_tipo());
            comprobante.setNotaReferenciaSerie(documento.getNota_referencia_serie());
            comprobante.setNotaReferenciaNumero(String.format("%08d", Integer.parseInt(documento.getNota_referencia_numero())));
        }

        //Operacion sujeta a detraccion
        List<FacturaElectronicaLeyenda> listLeyendas = new ArrayList<>();
        if (comprobante.getTipoOperacion().equals("1001") || comprobante.getTipoOperacion().equals("1002")) {
            comprobante.setCtaBancoNacionDetraccion(documento.getCta_banco_nacion_detraccion());
            comprobante.setPorDetraccion(documento.getPor_detraccion());
            comprobante.setMtoDetraccion(documento.getMto_detraccion());
            comprobante.setCodBienDetraccion(documento.getCod_bien_detraccion());

            listLeyendas.add(new FacturaElectronicaLeyenda("2006", "Operación sujeta a detracción"));
        }
        comprobante.setListFacturaElectronicaLeyendas(listLeyendas);

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

            BigDecimal afectacionIGVUnit = comprobanteDet.getAfectacion_igv().divide(comprobanteDet.getCantidad(), 2, RoundingMode.HALF_UP);
            det.setValorVentaItem(comprobanteDet.getPrecio_unitario().subtract(afectacionIGVUnit).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            det.setValorUnitario(comprobanteDet.getPrecio_unitario().subtract(afectacionIGVUnit).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            det.setAfectacionIgv(comprobanteDet.getAfectacion_igv().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            det.setAfectacionIGVCode(comprobanteDet.getTipo_igv());
            det.setDescuento(comprobanteDet.getDescuento_porcentaje().
                    divide(new BigDecimal(100)).
                    multiply(comprobanteDet.getCantidad().multiply(comprobanteDet.getPrecio_unitario())).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            det.setRecargo(comprobanteDet.getRecargo());
            det.setValorRefUnitario(comprobanteDet.getMonto_referencial_unitario());
            det.setCodTipTributoIgv(comprobanteDet.getCod_tributo_igv());
            list.add(det);

            baseamt = baseamt.add(comprobanteDet.getPrecio_unitario().multiply(comprobanteDet.getCantidad()).subtract(comprobanteDet.getAfectacion_igv()));
            taxtotal = taxtotal.add(comprobanteDet.getAfectacion_igv());
        }
        for (ComprobanteTax comprobanteTax : documento.getLista_tributos()) {
            FacturaElectronicaTax det = new FacturaElectronicaTax();
            det.setTaxId(comprobanteTax.getIde_tributo());
            det.setCodTipTributo(comprobanteTax.getCod_tip_tributo());
            det.setMtoBaseImponible(comprobanteTax.getMto_base_imponible());
            det.setMtoTributo(comprobanteTax.getMto_tributo());
            det.setNomTributo(comprobanteTax.getNom_tributo());

            listTax.add(det);
        }

//        facturaElectronicaTax = new FacturaElectronicaTax();
//        facturaElectronicaTax.setTaxId(9996);
//        facturaElectronicaTax.setBaseamt(new BigDecimal("1"));
//        facturaElectronicaTax.setTaxtotal(new BigDecimal("0"));
//        listTax.add(facturaElectronicaTax);
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
        comprobante.getListFacturaElectronicaLeyendas().stream().forEach(item -> {
            comprobante.addFacturaElectronicaLeyenda(item);
        });
        //constantes o bd
        comprobante.setIndSituacion("01");
        return comprobante;
    }

    @Override
    public String validarComprobante(ComprobanteCab documento, Empresa e) {
        if (documento.getCliente_tipo().equals("1")
                && String.format("%02d", Integer.parseInt(documento.getTipo())).equals("01")) {
            return "El dato ingresado en el tipo de documento de identidad del receptor no esta permitido para el tipo de comprobante";
        }
        if (documento.getCliente_tipo().equals("6") && documento.getCliente_documento().length() != 11) {
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
            if (item.getMonto_referencial_unitario() == null) {
                item.setMonto_referencial_unitario(BigDecimal.ZERO);
            }
            if (!(item.getUnidad_medida().equals("NIU") || item.getUnidad_medida().equals("ZZ"))) {
                return "Código de unidad de medida no soportado";
            }
            if (Integer.valueOf(item.getTipo_igv()) >= 10 && Integer.valueOf(item.getTipo_igv()) <= 20
                    && item.getAfectacion_igv() == BigDecimal.ZERO) {
                return "El monto de afectación de IGV por linea debe ser diferente a 0.00.";
            }
            if (item.getPrecio_unitario().compareTo(BigDecimal.ZERO) == 0
                    && !item.getTipo_igv().equals("31")) {
                return "El código de afectación igv del item no corresponde a una operación gratuita";
            }
            if (item.getPrecio_unitario().compareTo(BigDecimal.ZERO) == 0
                    && item.getMonto_referencial_unitario().compareTo(BigDecimal.ZERO) == 0) {
                return "El monto de valor referencial unitario debe ser mayor a 0.00 (Operaciones gratuitas)";
            }
            if (item.getPrecio_unitario().compareTo(BigDecimal.ZERO) == 0
                    && item.getMonto_referencial_unitario() != BigDecimal.ZERO) {
                return "El monto de valor referencial unitario debe ser 0.00 o null si el precio unitario es mayor a 0.00";
            }
        }
        //1. cambiar por clase catalogos
        //2. externalizar archivo
        List<String> listDocIds = Arrays.asList("0", "1", "4", "6", "7", "A");
        if (!listDocIds.contains(documento.getCliente_tipo())) {
            return "El tipo de documento de identidad no existe";
        }
        if (documento.getSerie_ref() != null && !documento.getSerie_ref().equals("")) {
            if (!documento.getNumero_ref().isEmpty() && !documento.getNumero_ref().isEmpty()) {
                FacturaElectronica fact = new FacturaElectronica();
                fact.setDocrefSerie(documento.getSerie_ref());
                fact.setDocrefNumero(String.format("%08d", Integer.parseInt(documento.getNumero_ref())));
                fact.setEmpresa(e);
                List<FacturaElectronica> comprobanteRel = facturaElectronicaRepository.findByDocrefSerieAndDocrefNumero(fact);
                if (comprobanteRel == null || (comprobanteRel != null && comprobanteRel.size() == 0)) {
                    return "Comprobante relacionado de anticipo no existe";
                }
            }
        }

        if (!(documento.getTipo().equals("07") || documento.getTipo().equals("08") || documento.getTipo().equals("03"))) {
            if (documento.getForma_pago().equalsIgnoreCase(Constantes.FORMA_PAGO_CONTADO) && !CollectionUtils.isEmpty(documento.getLista_cuotas())) {
                return "Si la forma de pago es Contado no es necesario indicar la lista de cuotas, campo: lista_cuotas";
            }
            if (documento.getForma_pago().equalsIgnoreCase(Constantes.FORMA_PAGO_CREDITO)) {
                for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {
                    Date fechaPago = null, fechaEmision = null;
                    try {
                        fechaPago = new SimpleDateFormat("dd/MM/yyyy").parse(detalle.getFecha_pago());
                        fechaEmision = new SimpleDateFormat("dd/MM/yyyy").parse(documento.getFecha_emision());
                    } catch (Exception ex) {
                        return "Si la forma de pago es Credito la fecha de pago no debe estar vacía y debe tener formato correcto dd/MM/yyyy, campo: fecha_pago";
                    }
                    if (FacturadorUtil.isNullOrEmpty(detalle.getMonto_pago())) {
                        return "Si la forma de pago es Credito debe indicar al monto de la cuota, campo: monto_pago";
                    }
                    if (FacturadorUtil.isNullOrEmpty(detalle.getTipo_moneda_pago())) {
                        return "Si la forma de pago es Credito debe indicar el tipo de moneda de la cuota, campo: tipo_moneda_pago";
                    }
                    if (fechaPago.compareTo(fechaEmision) <= 0) {
                        return "Si la forma de pago es Credito la fecha de pago de las cuotas debe ser mayor o igual a la fecha de emisión";
                    }
                }
            }

            if (!CollectionUtils.isEmpty(documento.getLista_cuotas())) {

                BigDecimal sumaCoutas = BigDecimal.ZERO;
                for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {

                    sumaCoutas = sumaCoutas.add(detalle.getMonto_pago());
                    
                }
                if (FacturadorUtil.isNullOrEmpty(documento.getMoneda_monto_neto_pendiente())) {
                    return "Si la forma de pago es Credito debe indicar el tipo de moneda del pago pendiente";
                }
                if (sumaCoutas.compareTo(documento.getMonto_neto_pendiente()) != 0) {
                    return "La suma de las cuotas debe ser igual al Monto neto pendiente de pago";

                }
            }
        }
        BigDecimal sumTotalTributos = BigDecimal.ZERO, sumBaseImponible = BigDecimal.ZERO;
        for (ComprobanteTax comprobanteTax : documento.getLista_tributos()) {
            for (ComprobanteDet item : documento.getLista_productos()) {
                if (item.getCod_tributo_igv().equals(comprobanteTax.getIde_tributo())) {
                    sumTotalTributos = sumTotalTributos.add(item.getAfectacion_igv());
                }
            }
            sumTotalTributos = sumTotalTributos.setScale(2, BigDecimal.ROUND_HALF_EVEN);
            if (comprobanteTax.getMto_tributo().compareTo(sumTotalTributos) != 0) {
                return "La suma del monto del tributo no equivale al del detalle. Código tributo: " + comprobanteTax.getIde_tributo();
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

    private Map<String, Object> genXmlAndSignAndValidate(String rootpath, FacturaElectronica documento,
            long ticket) throws IOException, XsdException, TransferirArchivoException, XsltException, Exception {
        Map<String, Object> retorno = new HashMap<>();
        XsltCpeValidator xsltCpeValidator = new XsltCpeValidator(this.xsltCpePath);
        XsdCpeValidator xsdCpeValidator = new XsdCpeValidator(this.xsltCpePath);

//        if (Constantes.CONSTANTE_SITUACION_POR_GENERAR_XML.equals(documento.getIndSituacion())
//                    || Constantes.CONSTANTE_SITUACION_CON_ERRORES.equals(documento.getIndSituacion())
//                    || Constantes.CONSTANTE_SITUACION_XML_VALIDAR.equals(documento.getIndSituacion())
//                    || Constantes.CONSTANTE_SITUACION_ENVIADO_RECHAZADO.equals(documento.getIndSituacion())
//                    || Constantes.CONSTANTE_SITUACION_ENVIADO_ANULADO.equals(documento.getIndSituacion())) {
//                retorno = "";
        String tipoComprobante = documento.getTipo();
        log.debug("BandejaDocumentosServiceImpl.generarComprobantePagoSunat...tipoComprobante: " + tipoComprobante);

        String nomFile = documento.getEmpresa().getNumdoc()
                + "-" + String.format("%02d", Integer.parseInt(documento.getTipo()))
                + "-" + documento.getSerie()
                + "-" + String.format("%08d", Integer.parseInt(documento.getNumero()));
        this.generarDocumentosService.formatoPlantillaXml(rootpath, documento, nomFile);

        retorno = this.generarDocumentosService.firmarXml(rootpath, documento.getEmpresa(), nomFile);

        xsdCpeValidator.validarSchemaXML(rootpath, documento.getTipo(), rootpath + "/" + documento.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml");

        xsltCpeValidator.validarXMLYComprimir(rootpath, documento.getEmpresa(), documento.getTipo(), rootpath + "/" + documento.getEmpresa().getNumdoc() + "/PARSE/", nomFile);

        documento.setXmlHash(retorno.get("xmlHash").toString());
        String tempDescripcionPluralMoneda = "SOLES";
        byte[] bytes = print(rootpath + "TEMP/", documento, 1, tempDescripcionPluralMoneda);

        retorno.put("pdfBase64", bytes);

        FileUtils.writeByteArrayToFile(new File(rootpath + "TEMP/" + nomFile + ".pdf"), bytes);

//        if (!appConfig.getEnvironment().equals("PRODUCTION")) {
//            FileUtils.writeByteArrayToFile(new File("D:/report.pdf"), bytes);
//        }
        String[] adjuntos = {rootpath + "/" + documento.getEmpresa().getNumdoc() + "/PARSE/" + nomFile + ".xml",
            rootpath + "TEMP/" + nomFile + ".pdf"};

        if (!documento.getClienteEmail().equals("")) {
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
                        documento.getClienteEmail(),
                        adjuntos));
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("BandejaDocumentosServiceImpl.generarComprobantePagoSunat...SendMail: Correo no enviado" + e.getMessage());
            }

        }
        LocalDateTime current = LocalDateTime.now().plusHours(appConfig.getDiferenceHours());

        documento.setFechaGenXml(current);
        documento.setIndSituacion(Constantes.CONSTANTE_SITUACION_XML_GENERADO);

        documento.setTicketOperacion(ticket);
//        if (documento.getTipo().equals("07") || documento.getTipo().equals("08")) {
//            documento.setListFacturaElectronicaCuotas(null);
//            documento.setListFacturaElectronicaLeyendas(null);
//            documento.setListFacturaElectronicaTax(null);
//        }
        save(documento);

//            }
        return retorno;
    }

    @Override
    public Map<String, Object> getPDF(long ticketOperacion, int tipo) throws Exception {
        Map<String, Object> retorno = new HashMap<>();
        List<FacturaElectronica> fact = getByTicketOperacion(ticketOperacion);
        if (fact == null) {
            return null;
        }
        if (fact.get(0).getId() == null) {
            return null;
        }
        log.info("fact.getId(): " + fact.get(0).getId());
        byte[] bytes = print(this.appConfig.getRootPath(), fact.get(0), tipo, "SOLES");
        if (!appConfig.getEnvironment().equals("PRODUCTION")) {
            FileUtils.writeByteArrayToFile(new File("D:/report.pdf"), bytes);
        }
        retorno.put("pdfBase64", bytes);
        return retorno;
    }

    @Override
    public byte[] getPDFInBtyes(long id, int tipo) throws Exception {
        Map<String, Object> retorno = new HashMap<>();
        FacturaElectronica fact = getById(id);
        if (fact == null) {
            return null;
        }
        if (fact.getId() == null) {
            return null;
        }
        log.info("fact.getId(): " + fact.getId());
        byte[] data = print(this.appConfig.getRootPath(), fact, tipo, "SOLES");
        if (!appConfig.getEnvironment().equals("PRODUCTION")) {
            FileUtils.writeByteArrayToFile(new File("C:/Users/user/Documents/report.pdf"), data);
        }
//        retorno.put("pdfBase64", bytes);
        return data;
    }
    
    private byte[] print(String rootpath, FacturaElectronica documento, int tipo, String tempDescripcionPluralMoneda) throws Exception {
        ByteArrayInputStream stream = Impresion.Imprimir(rootpath, tipo, documento, tempDescripcionPluralMoneda);
        int n = stream.available();
        byte[] bytes = new byte[n];
        stream.read(bytes, 0, n);
        return bytes;
    }

    @Override
    public List<FacturaElectronica> getByFechaEmisionBetweenAndEmpresaIdInAndEstadoIn(LocalDate fecIni, LocalDate fecFin, List<Integer> empresaIds, List<String> estados) {
        List<FacturaElectronica> list = facturaElectronicaRepository.findByFechaEmisionBetweenAndEmpresaIdInAndEstadoIn(fecIni, fecFin, empresaIds, estados);
        list.forEach((item) -> {
            item.setIndSituacion(item.getIndSituacion().equals("03") ? "ACEPTADO"
                    : item.getIndSituacion().equals("02") ? "XML generado"
                    : item.getIndSituacion().equals("01") ? "por generar XML"
                    : item.getIndSituacion().equals(Constantes.CONSTANTE_SITUACION_CON_ERRORES)
                    || item.getIndSituacion().equals(Constantes.CONSTANTE_SITUACION_ENVIADO_RECHAZADO)
                    ? item.getObservacionEnvio() : "Con problemas");
            item.setEstado(item.getEstado().equals("1") ? "ACTIVO" : "ANULADO");
            item.setTipo(Catalogos.tipoDocumento(item.getTipo(), null)[1]);
        });
        list = list.stream()
                .sorted(Comparator.comparing(FacturaElectronica::getNumero).reversed())
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<FacturaElectronica> getByIdIn(List<Long> ids) {
        List<FacturaElectronica> list = facturaElectronicaRepository.findByIdIn(ids);
        return list;
    }

    @Override
    public List<FacturaElectronica> getByTicketOperacion(long ticketOperacion) {
        return facturaElectronicaRepository.findByTicketOperacion(ticketOperacion);
    }

    @Override
    public void verifyPending() {
        List<String> listSituacion = new ArrayList<>();
        listSituacion.add(Constantes.CONSTANTE_SITUACION_XML_GENERADO);
        listSituacion.add(Constantes.CONSTANTE_SITUACION_CON_ERRORES);
        listSituacion.add(Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO);
        listSituacion.add(Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO_OBSERVACIONES);
        for (Empresa empresa : empresaService.getEmpresas()) {
            List<FacturaElectronica> list = facturaElectronicaRepository.
                    findToSendSunat(
                            empresa.getId(), Arrays.asList("01", "03"), listSituacion, "1", LocalDate.now().plusDays(-23));
            log.info("A enviar: " + String.valueOf(list.size()));
            DateTimeFormatter DD_MM_YYYY_FORMATER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String token = "";
            if (!list.isEmpty()) {
                try {
                    ValidacionSUNAT val = new ValidacionSUNAT("https://api-seguridad.sunat.gob.pe/v1/clientesextranet/815458f9-9a3d-4fe7-8135-ce68f2aa9ed6/oauth2/token/",
                            "https://api.sunat.gob.pe/v1/contribuyente/contribuyentes/" + empresa.getNumdoc() + "/validarcomprobante");
                    token = val.getApiToken("815458f9-9a3d-4fe7-8135-ce68f2aa9ed6", "ou/f6g6dPPSzpNnc8e4X2Q==");

                    for (FacturaElectronica facturaElectronica : list) {
                        try {
                            if (facturaElectronica.getFechaEmision().compareTo(LocalDate.now().plusDays(-1)) >= 0) {
                                continue;
                            }
                            ValidationBody valBody = new ValidationBody();
                            valBody.setNumRuc(empresa.getNumdoc());
                            valBody.setCodComp(facturaElectronica.getTipo());
                            valBody.setNumeroSerie(facturaElectronica.getSerie());
                            valBody.setNumero(facturaElectronica.getNumero());
                            valBody.setFechaEmision(facturaElectronica.getFechaEmision().format(DD_MM_YYYY_FORMATER));
                            valBody.setMonto(facturaElectronica.getTotalValorVenta().doubleValue());
                            String jsonBody = valBody.toJson(valBody);
                            ValidacionRespuestaApi validacionRespuestaApi = val.validate(token, jsonBody);
                            int cont = 0;
                            while (cont < 3) {
                                if (validacionRespuestaApi == null) {
                                    cont++;
                                    continue;
                                    
                                }
                                if (validacionRespuestaApi.isSuccess()) {
                                    if (validacionRespuestaApi.getData() != null && validacionRespuestaApi.getData().getEstadoCp() != null) {
                                        if (validacionRespuestaApi.getData().getEstadoCp().equals("1")) {
                                            facturaElectronica.setIndSituacion(Constantes.CONSTANTE_SITUACION_ENVIADO_ACEPTADO);
                                            facturaElectronica.setObservacionEnvio("VALIDADO Y ACEPTADO");
                                            save(facturaElectronica);
                                        } else {
                                            facturaElectronica.setIndSituacion(Constantes.CONSTANTE_SITUACION_XML_GENERADO);
                                            facturaElectronica.setObservacionEnvio("Comprobante no existe en SUNAT");
                                            save(facturaElectronica);
                                        }
                                        cont = 3;
                                        continue;
                                    }
                                }
                                cont++;
                            }

                            Thread.sleep(1000);
                        } catch (Exception e) {
                            String mensaje = "Hubo un problema al invocar servicio validación SUNAT: " + e.getMessage();
                            e.printStackTrace();
                            log.error(mensaje);
                            facturaElectronica.setFechaEnvio(LocalDateTime.now().plusHours(appConfig.getDiferenceHours()));
                            facturaElectronica.setIndSituacion(Constantes.CONSTANTE_SITUACION_CON_ERRORES);
                            facturaElectronica.setObservacionEnvio(mensaje);
                            facturaElectronica.setNroIntentoEnvio(facturaElectronica.getNroIntentoEnvio() + 1);
                            save(facturaElectronica);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        }
        log.debug("FacturaElectronicaServiceImpl.sendToSUNAT...enviarComprobantePagoSunat Final");
        //tipoindsituacion '06'
        //findByCurrentMonth
        //verify api rest sunat
        //1 => update 02, else send and update
    }
}

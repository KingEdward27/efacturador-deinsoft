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
import com.deinsoft.efacturador3.model.ResumenDiario;
import com.deinsoft.efacturador3.model.ResumenDiarioDet;
import com.deinsoft.efacturador3.model.ResumenDiarioTax;
import com.deinsoft.efacturador3.repository.ResumenDiarioRepository;
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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        cab.setNombreArchivo(empresa.getNumdoc() + "-"+ Constantes.CONSTANTE_TIPO_DOCUMENTO_RBOLETAS + "-"+ dateString + "-" + total);
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
                
                xsltCpeValidator.validarXMLYComprimir(rootpath, resumenDiario.getEmpresa(),tipo, rootpath + "/" + resumenDiario.getEmpresa().getNumdoc() + "/PARSE/", nomFile);
                
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
                
                resumenDiarioResult.setFechaGenXml(new Date());
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
}

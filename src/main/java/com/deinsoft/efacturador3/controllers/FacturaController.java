/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.service.BandejaDocumentosService;
import com.deinsoft.efacturador3.service.ComunesService;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.deinsoft.efacturador3.security.JwtUtil;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.GenerarDocumentosService;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author EDWARD-PC
 */
@RestController
@RequestMapping("document")
public class FacturaController {

    private static final Logger log = LoggerFactory.getLogger(FacturaController.class);

    @Autowired
    FacturaElectronicaService facturaElectronicaService;

    @Autowired
    EmpresaService empresaService;

    @Autowired
    private ComunesService comunesService;
    
    @Autowired
    private GenerarDocumentosService generarDocumentosService;
    
    @Autowired
    AppConfig appConfig;
    
    @PostMapping(value = "/send-document")
    public ResponseEntity<?> sendDocument(@Valid @RequestBody ComprobanteCab documento, 
            BindingResult bindingResult,HttpServletRequest request, HttpServletResponse response) throws TransferirArchivoException, ParseException {
//        FacturaElectronica facturaElectronicaResult = null;
        Map<String,Object> result = null;
        try {
            if (bindingResult.hasErrors()) {
                
                Map<String,Object> errors = this.validar(bindingResult);
                result  = new HashMap<>();
                result.put("code","001");
                result.put("message",errors);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
            }
            String mensajeValidacion = facturaElectronicaService.validarComprobante(documento);
            if (!mensajeValidacion.equals("")) {
                result  = new HashMap<>();
                result.put("code","002");
                result.put("message",mensajeValidacion);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
            }
            Map<String,Object> map = JwtUtil.getJwtLoggedUserData((HttpServletRequest)request);
            String numDoc = (String)map.get("numDoc");
            Empresa empresa = empresaService.findByNumdoc(numDoc);
            
            if(empresa == null || (empresa != null && empresa.getNumdoc() == null)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("La empresa no tiene permiso de registrar documentos");
            }
            FacturaElectronica comprobante = facturaElectronicaService.toFacturaModel(documento);
            comprobante.setEmpresa(empresa);

            List<FacturaElectronica> listFact = facturaElectronicaService.getBySerieAndNumeroAndEmpresaId(comprobante);
            if (listFact == null || (listFact != null && listFact.size() > 0)) {
                result  = new HashMap<>();
                result.put("code","002");
                result.put("message","El documento ya existe");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
            }
            if (comprobante.getTipo().equals("07") || comprobante.getTipo().equals("08")) {
                listFact = facturaElectronicaService.getByNotaReferenciaTipoAndNotaReferenciaSerieAndNotaReferenciaNumero(comprobante);
                if (listFact == null || (listFact != null && listFact.size() == 0)) {
                    result  = new HashMap<>();
                result.put("code","002");
                result.put("message","El documento referenciado no existe");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
                }

            }
//            facturaElectronicaResult = facturaElectronicaService.save(comprobante);

            result = this.facturaElectronicaService.generarComprobantePagoSunat(appConfig.getRootPath(), comprobante);
            
        } catch (Exception e) {
            e.printStackTrace();
            result  = new HashMap<>();
            result.put("code","003");
            result.put("message",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping(value = "/send-sunat")
    public ResponseEntity<?> enviarXML(@RequestParam(name = "id") String id,HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("FacturaController.send-sunat...Iniciando el procesamiento");
        HashMap<String, Object> resultado = new HashMap<>();
        String mensajeValidacion = "", resultadoProceso = "EXITO";
        log.debug("FacturaController.enviarXML...Consultar Comprobante");
        FacturaElectronica facturaElectronica = facturaElectronicaService.getById(Long.parseLong(id));
        try {
            log.debug("FacturaController.enviarXML...Enviar Comprobante");
            log.debug("FacturaController.enviarXML...enviarComprobantePagoSunat Inicio");
            HashMap<String, Object> retorno = new HashMap<>();
            HashMap<String, String> resultadoWebService = null;

            String nombreArchivo = appConfig.getRootPath() + "VALI/" + "constantes.properties";

            if ("02".equals(facturaElectronica.getIndSituacion()) || 
                "10".equals(facturaElectronica.getIndSituacion()) || 
                "06".equals(facturaElectronica.getIndSituacion())) {

//                Properties prop = this.comunesService.getProperties(nombreArchivo);
//
//                String urlWebService = (prop.getProperty("RUTA_SERV_CDP") != null) ? prop.getProperty("RUTA_SERV_CDP") : "XX";
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
                resultadoWebService = this.generarDocumentosService.enviarArchivoSunat(urlWebService, appConfig.getRootPath(), filename, facturaElectronica);

                retorno.put("resultadoWebService", resultadoWebService);
            }else {
                mensajeValidacion = "El documento se encuentra en una situación incrrecta o ya fue enviado";
                resultadoProceso = "-1";
            }

            log.debug("FacturaController.enviarXML...enviarComprobantePagoSunat Final");
            
            if (retorno != null) {

                resultadoWebService = (HashMap<String, String>) retorno.get("resultadoWebService");

                String estadoRetorno = (resultadoWebService.get("situacion") != null) ? (String) resultadoWebService.get("situacion") : "";

                String mensaje = (resultadoWebService.get("mensaje") != null) ? (String) resultadoWebService.get("mensaje") : "-";
                if (!"".equals(estadoRetorno)) {
                    if (!"11".equals(estadoRetorno)
                            && !"12".equals(estadoRetorno)) {
                        facturaElectronica.setFechaEnvio(new Date());
                        facturaElectronica.setIndSituacion(estadoRetorno);
                        facturaElectronica.setObservacionEnvio(mensaje);
                        facturaElectronicaService.save(facturaElectronica);
                    } else {
                        facturaElectronica.setIndSituacion(estadoRetorno);
                        facturaElectronica.setObservacionEnvio(mensaje);
                        facturaElectronicaService.save(facturaElectronica);
                    }
                }
                mensajeValidacion = mensaje;
                resultadoProceso = estadoRetorno;
            }
        } catch (Exception e) {
            String mensaje = "Hubo un problema al invocar servicio SUNAT: " + e.getMessage();
            e.printStackTrace();
            log.error(mensaje);
            facturaElectronica.setFechaEnvio(new Date());
            facturaElectronica.setIndSituacion("06");
            facturaElectronica.setObservacionEnvio(mensaje);
            facturaElectronicaService.save(facturaElectronica);
        }

        resultado.put("mensaje", mensajeValidacion);
        resultado.put("codigo", resultadoProceso);
        log.debug("SoftwareFacturadorController.enviarXML...Terminando el procesamiento");

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    protected Map<String, Object> validar(BindingResult result) {
        Map<String, Object> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), " El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return errores;
    }

    
}

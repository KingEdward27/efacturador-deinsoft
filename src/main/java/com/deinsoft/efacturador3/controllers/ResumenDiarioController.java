/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ResumenDiarioBean;
import com.deinsoft.efacturador3.bean.ResumenDiarioBeanDet;
import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.ResumenDiario;
import com.deinsoft.efacturador3.service.ComunesService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.ResumenDiarioService;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author EDWARD-PC
 */
@RestController
@RequestMapping("api/v1/resumenDiario")
public class ResumenDiarioController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ResumenDiarioController.class);

    @Autowired
    ResumenDiarioService resumenDiarioService;

    
    @Autowired
    AppConfig appConfig;

    
    
    @PostMapping(value = "send-document")
    public ResponseEntity<?> save(@Valid @RequestBody ResumenDiarioBean resumenDiarioBean, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws TransferirArchivoException, ParseException, IOException {
        Map<String, Object> result = null;
        try {
            if (bindingResult.hasErrors()) {

                Map<String, Object> errors = this.validar(bindingResult);
                result = new HashMap<>();
                result.put("code", "001");
                result.put("message", errors);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
            }
            Empresa empresa = getEmpresa(request);
            ResumenDiario resumenDiario = resumenDiarioService.toResumenDiarioModel(resumenDiarioBean,empresa);
            resumenDiario.setEmpresa(empresa);
            
            result = this.resumenDiarioService.generarComprobantePagoSunat(appConfig.getRootPath(), resumenDiario);
        } catch (Exception e) {
            e.printStackTrace();
            result = new HashMap<>();
            result.put("code", "003");
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    @PostMapping(value = "/send-sunat")
    public ResponseEntity<?> enviarXML(@RequestParam(name = "id") String id,HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("ResumenDiarioController.send-sunat...Iniciando el procesamiento");
        Map<String, Object> resultado = new HashMap<>();
        try {
            log.debug("ResumenDiarioController.enviarXML...Enviar Comprobante");
            resultado = resumenDiarioService.sendSUNAT(Long.parseLong(id));
        } catch (Exception e) {
            e.printStackTrace();
            resultado = new HashMap<>();
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
}

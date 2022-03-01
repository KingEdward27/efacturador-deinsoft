/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.service.BandejaDocumentosService;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
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

/**
 *
 * @author EDWARD-PC
 */
@RestController
@RequestMapping("empresa")
public class EmpresaController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    @Autowired
    EmpresaService empresaService;

    @PostMapping(value = "save")
    public ResponseEntity<?> save(@Valid @RequestBody Empresa empresa, BindingResult result) throws TransferirArchivoException, ParseException {
        HashMap<String, Object> resultado = null;
        String raiz = "D://DEFACT/";
//        File directorio=new File(raiz + empresa.getNumdoc());
//        directorio.mkdir();
        Empresa empresaResult = null;
        if(empresa.getIdempresa() != null && empresa.getIdempresa() > 0){
            empresaResult = empresaService.save(empresa);
            resultado.put("message", "Empresa actualizada!");
            resultado.put("empresa", empresaResult);
        }else{
            try {
                Empresa foundEmpresa = empresaService.findByNumdoc(empresa.getNumdoc());
                if(foundEmpresa != null || (foundEmpresa != null && foundEmpresa.getNumdoc() != null)){
                    return ResponseEntity.status(HttpStatus.FOUND).body("Ya se encuentra registrado el número de documento de la empresa");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocurrió un error inesperador al buscar la empresa");
            }
            try {
                HashMap<String, Object> param = new HashMap<>();
                param.put("nombreCertificado", empresa.getCertName());
                param.put("passPrivateKey", empresa.getCertPass());
                param.put("numDoc",empresa.getNumdoc());
                param.put("rootPath",raiz);
                resultado = (new CertificadoFacturador()).importarCertificado(param);
            } catch (Exception e) {
                resultado = new HashMap<>();
                resultado.put("validacion", e.getMessage());
                log.error(e.getMessage());
            }
            if(resultado != null ){
                String message = (resultado.get("validacion") != null) ? (String) resultado.get("validacion") : "";
                if(!message.equalsIgnoreCase("EXITO")){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
                }
            }
            empresaResult = empresaService.save(empresa);
            resultado.put("message", "Empresa creada!, se agregó correctamente la clave privada");
            resultado.put("empresa", empresaResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
}

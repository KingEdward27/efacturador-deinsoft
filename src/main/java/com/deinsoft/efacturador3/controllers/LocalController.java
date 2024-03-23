/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.Local;
import com.deinsoft.efacturador3.security.SecurityConstants;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.service.LocalService;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author EDWARD-PC
 */
@RestController
@RequestMapping("api/v1/local")
public class LocalController {

    private static final Logger log = LoggerFactory.getLogger(LocalController.class);

    @Autowired
    LocalService localService;

    @Autowired
    AppConfig appConfig;

    @PostMapping(value = "save")
    public ResponseEntity<?> save(
            @Valid @RequestBody Local local, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(localService.save(local));
    }
    
    @GetMapping(value = "list")
    public ResponseEntity<?> list() throws Exception {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(localService.getLocales());
    }
   
}

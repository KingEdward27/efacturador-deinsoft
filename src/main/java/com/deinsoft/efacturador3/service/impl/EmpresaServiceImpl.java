/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.repository.EmpresaRepository;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.service.FileStorageService;
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import javax.mail.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author EDWARD-PC
 */

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
    
    @Autowired
    EmpresaRepository empresaRepository;
    
    @Autowired
    AppConfig appConfig;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Override
    public Empresa getEmpresaById(int id) {
        return empresaRepository.getById(id);
    }

    @Override
    public List<Empresa> getEmpresas() {
        return empresaRepository.findAll();
    }

    @Override
    public void createDirsIfNotExists(Empresa empresa) {
        String basePath = appConfig.getRootPath()+ "/" + empresa.getNumdoc();
        File directorio = new File(basePath);
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                log.info("Directorio " + basePath + " creado");
            }
        }
        
        directorio = new File(basePath + "/PARSE");
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                log.info("Directorio " + basePath + "/PARSE" + " creado");
            }
        }
        
        directorio = new File(basePath + "/FIRMA");
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                log.info("Directorio " + basePath + "/FIRMA" + " creado");
            }
        }
        
        directorio = new File(basePath + "/RPTA");
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                log.info("Directorio " + basePath + "/ENVIO" + " creado");
            }
        }
        
        
        directorio = new File(basePath + "/CERT");
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                log.info("Directorio " + basePath + "/CERT" + " creado");
            }
        }
        
        directorio = new File(basePath + "/TEMP");
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                log.info("Directorio " + basePath + "/TEMP" + " creado");
            }
        }
        
        directorio = new File(basePath + "/ENVIO");
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                log.info("Directorio " + basePath + "/ENVIO" + " creado");
            }
        }
        
    }
    
    @Override
    public void prepareAndUpload(Empresa empresa,MultipartFile file) {
        createDirsIfNotExists(empresa);
        fileStorageService.storeFile(empresa.getNumdoc() + "/CERT", file);
    }
    
    @Override
    public Empresa save(Empresa empresa) {
        return empresaRepository.save(empresa);
    }
    @Override
    public Empresa findByNumdoc(String numdoc){
        return empresaRepository.findByNumdoc(numdoc);
    }

    @Override
    public Empresa findByToken(String token) {
        return empresaRepository.findByToken(token);
    }
    
}

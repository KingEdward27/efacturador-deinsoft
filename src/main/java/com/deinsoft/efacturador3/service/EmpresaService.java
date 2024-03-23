/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.Empresa;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author EDWARD-PC
 */
public interface EmpresaService {

    public Empresa getEmpresaById(int id);

    public List<Empresa> getEmpresas();

    public void prepareAndUpload(Empresa empresa,MultipartFile file);
    
    public Empresa save(Empresa empresa);

    public Empresa findByNumdoc(String numdoc);

    public Empresa findByToken(String token);

    void createDirsIfNotExists(Empresa empresa);
    
    
}

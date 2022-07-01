/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.repository.EmpresaRepository;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author EDWARD-PC
 */

@Service
public class EmpresaServiceImpl implements EmpresaService{

    private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
    
    @Autowired
    EmpresaRepository empresaRepository;
    
    @Override
    public Empresa getEmpresaById(int id) {
        return empresaRepository.getById(id);
    }

    @Override
    public List<Empresa> getEmpresas() {
        return empresaRepository.findAll();
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

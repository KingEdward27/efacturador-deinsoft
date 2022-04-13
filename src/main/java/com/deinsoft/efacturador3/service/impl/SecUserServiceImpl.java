/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.model.SecUser;
import com.deinsoft.efacturador3.repository.SecUserRepository;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.service.SecUserService;
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
public class SecUserServiceImpl implements SecUserService{

    private static final Logger log = LoggerFactory.getLogger(SecUserServiceImpl.class);
    
    @Autowired
    SecUserRepository secUserRepository;
    
    @Override
    public SecUser getSecUserById(long id) {
        return secUserRepository.getById(id);
    }

    @Override
    public List<SecUser> getSecUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SecUser save(SecUser secUser) {
        return secUserRepository.save(secUser);
    }
}

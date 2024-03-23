/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.model.Local;
import com.deinsoft.efacturador3.repository.LocalRepository;
import com.deinsoft.efacturador3.service.LocalService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author user
 */
@Service
public class LocalServiceImpl implements LocalService{

    @Autowired
    LocalRepository localRepository;
    
    @Override
    public List<Local> getLocales() {
        return localRepository.findAll();
    }

    @Override
    public Local save(Local local) {
        return localRepository.save(local);
    }
    
}

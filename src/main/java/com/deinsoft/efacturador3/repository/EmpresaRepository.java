/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author EDWARD-PC
 */
public interface EmpresaRepository extends JpaRepository<Empresa,Integer> {
    Empresa findByNumdoc(String numdoc);
    
    Empresa findByToken(String token);
}

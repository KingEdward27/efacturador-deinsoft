/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.model.ResumenBaja;
import com.deinsoft.efacturador3.model.ResumenDiario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author EDWARD-PC
 */
public interface ResumenDiarioRepository extends JpaRepository<ResumenDiario,Long> {
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.model.ResumenBaja;
import com.deinsoft.efacturador3.model.ResumenDiario;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author EDWARD-PC
 */
public interface ResumenDiarioRepository extends JpaRepository<ResumenDiario,Long> {
    List<ResumenDiario> findByFechaEmision (Date fecha);
    
    @Query(value="select c from resumenDiarioDet d "
            + "join resumenDiario c on d.resumenDiario.id = c.id "
            + "where d.nroDocumento = :numero")
    ResumenDiario findResumenDiarioByComprobante(@Param("numero") String numero );
}

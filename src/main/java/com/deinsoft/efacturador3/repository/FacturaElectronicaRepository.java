/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.FacturaElectronica;
import com.deinsoft.efacturador3.model.Documento;
import com.deinsoft.efacturador3.model.DocumentoPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author EDWARD-PC
 */
public interface FacturaElectronicaRepository extends JpaRepository<FacturaElectronica,Long> {
//    @Query(value = "SELECT D FROM DOCUMENTO D WHERE NUM_RUC = :#{#paramDocumento.num_ruc} AND TIP_DOCU = :#{#paramDocumento.tip_docu} AND NUM_DOCU = :#{#paramDocumento.num_docu}")
//    List<Documento> consultarBandejaPorId(@Param("paramDocumento") Documento paramDocumento);
//    
//    @Query(value = "SELECT COUNT(1) FROM DOCUMENTO D WHERE NOM_ARCH = :#{#paramDocumento.nom_arch}")
//    Integer contarBandejaPorNomArch(@Param("paramDocumento") Documento paramDocumento);
//  
//    @Query(value = "SELECT D FROM DOCUMENTO D WHERE IND_SITU = :#{#paramDocumento.ind_situ}")
//    List<Documento> consultarBandejaPorSituacion(@Param("paramDocumento") Documento paramDocumento);
}

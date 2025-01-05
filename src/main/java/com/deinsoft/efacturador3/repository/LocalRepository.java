/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.dto.NumeroDocumentoDto;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.Local;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author EDWARD-PC
 */
public interface LocalRepository extends JpaRepository<Local,Integer> {
    @Query(value = "select p from local p\n" +
                    "where empresa_id = :empresaId\n" +
                    "and serie_relacion = :serieRelacion")
    List<Local> getByEmpresaIdAndSerieRelacion(@Param("empresaId") long empresaId,@Param("serieRelacion") String serieRelacion);
}

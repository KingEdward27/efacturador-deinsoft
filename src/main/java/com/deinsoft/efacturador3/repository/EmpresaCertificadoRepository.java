package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.model.EmpresaCertificado;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpresaCertificadoRepository extends JpaRepository<EmpresaCertificado, Integer> {

    List<EmpresaCertificado> findByEmpresaId(Integer empresaId);

    List<EmpresaCertificado> findByEmpresaNumdoc(String numdoc);

    @Query("SELECT ec FROM EmpresaCertificado ec WHERE ec.empresa.numdoc = :numdoc " +
           "AND ec.fechaInicioVigencia <= :now AND ec.fechaFinVigencia >= :now " +
           "ORDER BY ec.fechaFinVigencia DESC")
    List<EmpresaCertificado> findVigentesByNumdoc(@Param("numdoc") String numdoc, @Param("now") Date now);
}

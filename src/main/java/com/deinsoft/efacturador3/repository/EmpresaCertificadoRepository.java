package com.deinsoft.efacturador3.repository;

import com.deinsoft.efacturador3.model.EmpresaCertificado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaCertificadoRepository extends JpaRepository<EmpresaCertificado, Integer> {

    List<EmpresaCertificado> findByEmpresaId(Integer empresaId);

    List<EmpresaCertificado> findByEmpresaNumdoc(String numdoc);
}

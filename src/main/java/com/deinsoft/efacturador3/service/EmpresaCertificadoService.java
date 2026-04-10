package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.EmpresaCertificado;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface EmpresaCertificadoService {

    EmpresaCertificado save(EmpresaCertificado empresaCertificado);

    List<EmpresaCertificado> findByEmpresaId(Integer empresaId);

    List<EmpresaCertificado> findByEmpresaNumdoc(String numdoc);

    List<EmpresaCertificado> findAll();

    EmpresaCertificado importarCertificado(String numdoc, String passPrivateKey, MultipartFile file) throws Exception;
}

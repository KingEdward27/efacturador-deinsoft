package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.EmpresaCertificado;
import com.deinsoft.efacturador3.repository.EmpresaCertificadoRepository;
import com.deinsoft.efacturador3.repository.EmpresaRepository;
import com.deinsoft.efacturador3.service.EmpresaCertificadoService;
import com.deinsoft.efacturador3.service.FileStorageService;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmpresaCertificadoServiceImpl implements EmpresaCertificadoService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaCertificadoServiceImpl.class);

    @Autowired
    EmpresaCertificadoRepository empresaCertificadoRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AppConfig appConfig;

    @Override
    public EmpresaCertificado save(EmpresaCertificado empresaCertificado) {
        return empresaCertificadoRepository.save(empresaCertificado);
    }

    @Override
    public List<EmpresaCertificado> findByEmpresaId(Integer empresaId) {
        return empresaCertificadoRepository.findByEmpresaId(empresaId);
    }

    @Override
    public List<EmpresaCertificado> findByEmpresaNumdoc(String numdoc) {
        return empresaCertificadoRepository.findByEmpresaNumdoc(numdoc);
    }

    @Override
    public List<EmpresaCertificado> findAll() {
        return empresaCertificadoRepository.findAll();
    }

    @Override
    public EmpresaCertificado importarCertificado(String numdoc, String passPrivateKey, MultipartFile file) throws Exception {
        // 1. Obtener empresa por numdoc
        Empresa empresa = empresaRepository.findByNumdoc(numdoc);
        if (empresa == null) {
            throw new Exception("No se encontró la empresa con RUC: " + numdoc);
        }

        String certName = file.getOriginalFilename();

        // 2. Subir archivo a {rootPath}/{numdoc}/CERT/
        fileStorageService.storeFile(numdoc + "/" + Constantes.CONSTANTE_CERT, file, certName);
        log.info("importarCertificado - archivo subido: {}/{}/{}", numdoc, Constantes.CONSTANTE_CERT, certName);

        String rutaCertificado = appConfig.getRootPath() + numdoc + "/" + Constantes.CONSTANTE_CERT + "/" + certName;

        // 3. Leer alias y fechas de vigencia del PFX
        CertificadoFacturador certificadoFacturador = new CertificadoFacturador();
        HashMap<String, Object> infoCert = certificadoFacturador.leerInfoCertificado(rutaCertificado, passPrivateKey);
        if (infoCert.isEmpty()) {
            throw new Exception("No se pudo leer la información del certificado. Verifique el archivo y la contraseña.");
        }
        String aliasPfx = (String) infoCert.get("alias");
        Date fechaInicio = (Date) infoCert.get("fechaInicio");
        Date fechaFin = (Date) infoCert.get("fechaFin");

        // 4. Validar que el certificado corresponde al RUC de la empresa
        String outputValidacion = certificadoFacturador.validaCertificado(rutaCertificado, numdoc, passPrivateKey);
        log.info("importarCertificado - validaCertificado: {}", outputValidacion);
        if (!outputValidacion.contains("[ALIAS]")) {
            throw new Exception("El certificado no está configurado con el RUC de la empresa: " + numdoc);
        }

        // 5. Importar al JKS compartido
        HashMap<String, Object> param = new HashMap<>();
        param.put("nombreCertificado", certName);
        param.put("passPrivateKey", passPrivateKey);
        param.put("numDoc", numdoc);
        param.put("rootPath", appConfig.getRootPath());
        HashMap<String, Object> resultado = certificadoFacturador.importarCertificado(param);

        String validacion = (resultado.get("validacion") != null) ? (String) resultado.get("validacion") : "";
        if (!"EXITO".equalsIgnoreCase(validacion)) {
            throw new Exception("Error al importar certificado al keystore: " + validacion);
        }

        // 6. Registrar en tabla empresa_certificado
        EmpresaCertificado ec = new EmpresaCertificado();
        ec.setEmpresa(empresa);
        ec.setNombre(certName);
        ec.setAlias(Constantes.PRIVATE_KEY_ALIAS + numdoc);
        ec.setPassword(FacturadorUtil.Encriptar(passPrivateKey));
        ec.setFechaInicioVigencia(fechaInicio);
        ec.setFechaFinVigencia(fechaFin);
        String subjectDN = infoCert.get("subjectDN") != null ? (String) infoCert.get("subjectDN") : aliasPfx;
        ec.setDetalle(subjectDN);

        EmpresaCertificado saved = empresaCertificadoRepository.save(ec);
        log.info("importarCertificado - registro guardado id: {}", saved.getId());
        return saved;
    }
}

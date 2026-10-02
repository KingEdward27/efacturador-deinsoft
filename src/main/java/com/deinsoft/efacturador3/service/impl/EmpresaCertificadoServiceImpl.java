package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.EmpresaCertificado;
import com.deinsoft.efacturador3.repository.EmpresaCertificadoRepository;
import com.deinsoft.efacturador3.repository.EmpresaRepository;
import com.deinsoft.efacturador3.security.SecurityConstants;
import com.deinsoft.efacturador3.service.EmpresaCertificadoService;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.service.FileStorageService;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.text.SimpleDateFormat;
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
    EmpresaService empresaService;

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
    public EmpresaCertificado importarCertificado(String numdoc, String razonSocial, String nombreComercial,
            Integer tipodoc, String usuariosol, String clavesol, String direccion,
            String passPrivateKey, MultipartFile file) throws Exception {

        // 1. Buscar empresa; si no existe, crearla con sus carpetas
        Empresa empresa = empresaRepository.findByNumdoc(numdoc);
        boolean esNueva = (empresa == null);

        if (esNueva) {
            empresa = new Empresa();
            empresa.setNumdoc(numdoc);
            empresa.setRazonSocial(razonSocial != null ? razonSocial : "");
            empresa.setNombreComercial(nombreComercial != null ? nombreComercial : "");
            empresa.setTipodoc(tipodoc != null ? tipodoc : 6);
            empresa.setUsuariosol(usuariosol != null ? usuariosol : "");
            empresa.setClavesol(clavesol != null ? FacturadorUtil.Encriptar(clavesol) : "");
            empresa.setDireccion(direccion != null ? direccion : "");
            log.info("importarCertificado - empresa nueva, registrando RUC: {}", numdoc);
        }

        // 2. Crear carpetas si no existen
        empresaService.createDirsIfNotExists(empresa);

        // 3. Subir archivo con nombre único a {rootPath}/{numdoc}/CERT/
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "certificado";
        String ext = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String baseName = originalName.contains(".") ? originalName.substring(0, originalName.lastIndexOf(".")) : originalName;
        String certName = baseName + "_" + System.currentTimeMillis() + ext;

        fileStorageService.storeFile(numdoc + "/" + Constantes.CONSTANTE_CERT, file, certName);
        log.info("importarCertificado - archivo subido: {}/{}/{}", numdoc, Constantes.CONSTANTE_CERT, certName);

        String rutaCertificado = appConfig.getRootPath() + numdoc + "/" + Constantes.CONSTANTE_CERT + "/" + certName;

        // 4. Leer alias y fechas de vigencia del PFX
        CertificadoFacturador cf = new CertificadoFacturador();
        HashMap<String, Object> infoCert = cf.leerInfoCertificado(rutaCertificado, passPrivateKey);
        if (infoCert.isEmpty()) {
            throw new Exception("No se pudo leer la información del certificado. Verifique el archivo y la contraseña.");
        }
        Date fechaInicio = (Date) infoCert.get("fechaInicio");
        Date fechaFin = (Date) infoCert.get("fechaFin");

        // 5. Validar que el certificado corresponde al RUC
        String outputValidacion = cf.validaCertificado(rutaCertificado, numdoc, passPrivateKey);
        log.info("importarCertificado - validaCertificado: {}", outputValidacion);
        if (!outputValidacion.contains("[ALIAS]")) {
            throw new Exception("El certificado no está configurado con el RUC de la empresa: " + numdoc);
        }

        // 6. Importar al JKS compartido
        String alias = Constantes.PRIVATE_KEY_ALIAS + numdoc
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        HashMap<String, Object> param = new HashMap<>();
        param.put("nombreCertificado", certName);
        param.put("passPrivateKey", passPrivateKey);
        param.put("numDoc", numdoc);
        param.put("rootPath", appConfig.getRootPath());
        param.put("alias", alias);
        HashMap<String, Object> resultado = cf.importarCertificado(param);
        String validacion = (resultado.get("validacion") != null) ? (String) resultado.get("validacion") : "";
        if (!"EXITO".equalsIgnoreCase(validacion)) {
            throw new Exception("Error al importar certificado al keystore: " + validacion);
        }

        // 7. Si es empresa nueva: generar token y guardar
        if (esNueva) {
            long expiresIn = SecurityConstants.TOKEN_EXPIRATION_TIME * 1000L * 1000L * 1000L;
            String token = Jwts.builder()
                    .setIssuedAt(new Date())
                    .setIssuer(SecurityConstants.ISSUER_INFO)
                    .setId("DEFACT-JWT")
                    .setSubject(empresa.getNumdoc() + "/" + empresa.getRazonSocial())
                    .claim("numDoc", empresa.getNumdoc())
                    .claim("razonSocial", empresa.getRazonSocial())
                    .claim("usuarioSol", empresa.getUsuariosol())
                    .setExpiration(new Date(new Date().getTime() + expiresIn))
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.SUPER_SECRET_KEY)
                    .compact();
            empresa.setToken(token);
            empresa = empresaRepository.save(empresa);
            log.info("importarCertificado - empresa nueva guardada id: {}", empresa.getId());
        }

        // 8. Registrar en tabla empresa_certificado
        EmpresaCertificado ec = new EmpresaCertificado();
        ec.setEmpresa(empresa);
        ec.setNombre(certName);
        ec.setAlias(alias);
        ec.setPassword(FacturadorUtil.Encriptar(passPrivateKey));
        ec.setFechaInicioVigencia(fechaInicio);
        ec.setFechaFinVigencia(fechaFin);
        String subjectDN = infoCert.get("subjectDN") != null ? (String) infoCert.get("subjectDN") : "";
        ec.setDetalle(subjectDN + " | archivo-original: " + originalName);

        EmpresaCertificado saved = empresaCertificadoRepository.save(ec);
        log.info("importarCertificado - registro guardado id: {}, esNueva: {}", saved.getId(), esNueva);
        return saved;
    }

    @Override
    public String getAccessToken(String numdoc) {
        Empresa empresa = empresaRepository.findByNumdoc(numdoc);
        return empresa != null ? empresa.getToken() : null;
    }
}

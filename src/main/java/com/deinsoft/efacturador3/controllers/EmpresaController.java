/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.security.SecurityConstants;
import com.deinsoft.efacturador3.util.CertificadoFacturador;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
//import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author EDWARD-PC
 */
@RestController
@RequestMapping("api/v1/empresa")
public class EmpresaController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    @Autowired
    EmpresaService empresaService;

    @Autowired
    AppConfig appConfig;

    @PostMapping(value = "save", consumes = { "multipart/form-data" })
    public ResponseEntity<?> save(
            @Valid @RequestPart("empresa") Empresa empresa , 
            @RequestPart("file") MultipartFile file , BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HashMap<String, Object> resultado = new HashMap<>();
        Empresa foundEmpresa = null;

        //valdiate exists
        try {
            if (file.isEmpty()) {
                resultado = new HashMap<>();
                resultado.put("code", "001");
                resultado.put("message", "Debe adjuntar certificado digital SUNAT");
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resultado);
            }
            
            if (!(empresa.getId() != null && empresa.getId() > 0)) {
                foundEmpresa = empresaService.findByNumdoc(empresa.getNumdoc());
                if (foundEmpresa != null || (foundEmpresa != null && foundEmpresa.getNumdoc() != null)) {
                    resultado = new HashMap<>();
                    resultado.put("code", "001");
                    resultado.put("message", "Ya se encuentra registrado el número de documento de la empresa");
                    return ResponseEntity.status(HttpStatus.FOUND).body(resultado);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultado.put("code", "002");
            resultado.put("message", "Ocurrió un error inesperado al buscar la empresa");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
        
        //create dirs and upload cert
        try {
            empresaService.prepareAndUpload(empresa,file);
        } catch (Exception e) {
            e.printStackTrace();
            resultado.put("code", "003");
            resultado.put("message", "Ocurrió un error al preparar directorios y subir certificado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
        
        //validate and import cert
        try {
            HashMap<String, Object> param = new HashMap<>();
            param.put("nombreCertificado", empresa.getCertName());
            param.put("passPrivateKey", empresa.getCertPass());
            param.put("numDoc", empresa.getNumdoc());
            param.put("rootPath", appConfig.getRootPath());
            resultado = (new CertificadoFacturador()).importarCertificado(param);
        } catch (Exception e) {
            resultado = new HashMap<>();
            resultado.put("code", "004");
            resultado.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
        if (resultado != null) {
            String message = (resultado.get("validacion") != null) ? (String) resultado.get("validacion") : "";
            if (!message.equalsIgnoreCase("EXITO")) {
                resultado = new HashMap<>();
                resultado.put("code", "005");
                resultado.put("message", message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
            }
        }
        long expiresIn = SecurityConstants.TOKEN_EXPIRATION_TIME * 1000 * 1000 * 1000;
        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setIssuer(SecurityConstants.ISSUER_INFO)
                .setId("DEFACT-JWT")
                .setSubject(empresa.getNumdoc() + "/" + empresa.getRazonSocial())
                .claim("numDoc", empresa.getNumdoc())
                .claim("razonSocial", empresa.getRazonSocial())
                .claim("usuarioSol", empresa.getUsuariosol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiresIn))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SUPER_SECRET_KEY).compact();

        empresa.setToken(token);
        empresa.setCertPass(FacturadorUtil.Encriptar(empresa.getCertPass()));
        
        //save or update empresa
        Empresa empresaResult = empresaService.save(empresa);
        
        resultado.put("code", "000");
        resultado.put("message", "Empresa creada/actualizada!, se agregó correctamente la clave privada");
        resultado.put("id", empresaResult.getId());
        resultado.put("access_token", empresaResult.getToken());
        resultado.put("token_type", "JWT");
        resultado.put("expires_in", expiresIn/ 1000);
        resultado.put("expires_date", new Date(new Date().getTime() + expiresIn));

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PostMapping(value = "/update-token")
    public ResponseEntity<?> generateToken(@RequestParam(name = "id") String id,
            HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        HashMap<String, Object> resultado = new HashMap<>();
//        File directorio=new File(raiz + empresa.getNumdoc());
//        directorio.mkdir();
        Empresa empresa = empresaService.getEmpresaById(Integer.parseInt(id));
        String token = generateToken(empresa);

        log.info("********* TOKEN: {}", SecurityConstants.TOKEN_BEARER_PREFIX + " " + token);
//		LOGGER.info( "********* uuid mongodb: {}" , session.getId());
        response.addHeader(SecurityConstants.HEADER_AUTHORIZACION_KEY, SecurityConstants.TOKEN_BEARER_PREFIX + " " + token);
//		response.addHeader("sessionId", session.getId());
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");

        empresa.setToken(token);
        
        //save or update empresa
        Empresa empresaResult = empresaService.save(empresa);
        resultado.put("message", "Empresa actualizada!, se actualizó el token");
        resultado.put("empresa", empresaResult);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    String generateToken(Empresa empresa) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setIssuer(SecurityConstants.ISSUER_INFO)
                .setId("DEFACT-JWT")
                .setSubject(empresa.getNumdoc() + "/" + empresa.getRazonSocial())
                //				.claim("empresaId", empresa.getIdempresa())
                //                                .claim("empresaId", empresa.getIdempresa()) 
                .claim("numDoc", empresa.getNumdoc()) //((User)auth.getPrincipal()).getAuthorities())
                //				.claim("authorities", empresa.getNumdoc())
                .claim("razonSocial", empresa.getRazonSocial()) //((User)auth.getPrincipal()).getAuthorities())
                .claim("usuarioSol", empresa.getUsuariosol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + SecurityConstants.TOKEN_EXPIRATION_TIME * 1000 * 1000 * 1000))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SUPER_SECRET_KEY).compact();
    }
}

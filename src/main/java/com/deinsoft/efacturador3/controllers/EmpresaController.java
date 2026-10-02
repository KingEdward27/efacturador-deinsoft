/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.security.SecurityConstants;
import com.deinsoft.efacturador3.service.EmpresaCertificadoService;
import com.deinsoft.efacturador3.service.EmpresaService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
    EmpresaCertificadoService empresaCertificadoService;

    @Autowired
    AppConfig appConfig;

    @PostMapping(value = "save", consumes = { "multipart/form-data" })
    public ResponseEntity<?> save(
            @Valid @RequestPart("empresa") Empresa empresa,
            @RequestPart("certPass") String certPass,
            @RequestPart("file") MultipartFile file,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        HashMap<String, Object> resultado = new HashMap<>();

        if (file.isEmpty()) {
            resultado.put("code", "001");
            resultado.put("message", "Debe adjuntar certificado digital SUNAT");
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resultado);
        }

        // Para registros nuevos, verificar que la empresa no exista ya
        if (!(empresa.getId() != null && empresa.getId() > 0)) {
            try {
                Empresa found = empresaService.findByNumdoc(empresa.getNumdoc());
                if (found != null) {
                    resultado.put("code", "001");
                    resultado.put("message", "Ya se encuentra registrado el número de documento de la empresa");
                    return ResponseEntity.status(HttpStatus.FOUND).body(resultado);
                }
            } catch (Exception e) {
                resultado.put("code", "002");
                resultado.put("message", "Ocurrió un error inesperado al buscar la empresa");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
            }
        }

        try {
            empresaCertificadoService.importarCertificado(
                    empresa.getNumdoc(),
                    empresa.getRazonSocial(),
                    empresa.getNombreComercial(),
                    empresa.getTipodoc(),
                    empresa.getUsuariosol(),
                    empresa.getClavesol(),
                    empresa.getDireccion(),
                    certPass,
                    file);

            long expiresIn = SecurityConstants.TOKEN_EXPIRATION_TIME * 1000L * 1000L * 1000L;
            String accessToken = empresaCertificadoService.getAccessToken(empresa.getNumdoc());

            resultado.put("code", "000");
            resultado.put("validacion", "EXITO");
            resultado.put("message", "Empresa creada/actualizada!, se agregó correctamente la clave privada");
            resultado.put("access_token", accessToken);
            resultado.put("token_type", "JWT");
            resultado.put("expires_in", expiresIn / 1000);
            resultado.put("expires_date", new Date(new Date().getTime() + expiresIn));
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (Exception e) {
            log.error("save empresa error: {}", e.getMessage(), e);
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam(name = "numdoc") String numdoc,
            HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, Object> resultado = new HashMap<>();
        Empresa empresa = empresaService.findByNumdoc(numdoc);
        if (empresa == null) {
            resultado.put("code", "001");
            resultado.put("message", "No se encontró la empresa con RUC: " + numdoc);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        String token = generateToken(empresa);
        empresa.setToken(token);
        empresaService.save(empresa);
        resultado.put("code", "000");
        resultado.put("access_token", token);
        return ResponseEntity.ok(resultado);
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

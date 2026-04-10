package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.model.EmpresaCertificado;
import com.deinsoft.efacturador3.service.EmpresaCertificadoService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/empresa-certificado")
public class EmpresaCertificadoController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaCertificadoController.class);

    @Autowired
    EmpresaCertificadoService empresaCertificadoService;

    @PostMapping(value = "importar", consumes = {"multipart/form-data"})
    public ResponseEntity<?> importar(
            @RequestPart("numdoc") String numdoc,
            @RequestPart("passPrivateKey") String passPrivateKey,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {

        HashMap<String, Object> resultado = new HashMap<>();

        if (file.isEmpty()) {
            resultado.put("code", "001");
            resultado.put("message", "Debe adjuntar el certificado digital (.pfx o .p12)");
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resultado);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.toLowerCase().endsWith(".pfx") && !fileName.toLowerCase().endsWith(".p12"))) {
            resultado.put("code", "002");
            resultado.put("message", "El archivo debe ser de tipo .pfx o .p12");
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resultado);
        }

        try {
            EmpresaCertificado ec = empresaCertificadoService.importarCertificado(numdoc, passPrivateKey, file);
            resultado.put("code", "000");
            resultado.put("message", "Certificado importado y registrado correctamente");
            resultado.put("id", ec.getId());
            resultado.put("alias", ec.getAlias());
            resultado.put("fechaInicioVigencia", ec.getFechaInicioVigencia());
            resultado.put("fechaFinVigencia", ec.getFechaFinVigencia());
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (Exception e) {
            log.error("importar certificado - error: {}", e.getMessage(), e);
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
    }

    @GetMapping(value = "list-by-numdoc")
    public ResponseEntity<?> listByNumdoc(@RequestParam String numdoc) {
        return ResponseEntity.status(HttpStatus.OK).body(empresaCertificadoService.findByEmpresaNumdoc(numdoc));
    }

    @PostMapping(value = "save")
    public ResponseEntity<?> save(
            @Valid @RequestBody EmpresaCertificado empresaCertificado, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaCertificadoService.save(empresaCertificado));
    }

    @GetMapping(value = "list")
    public ResponseEntity<?> list() {
        return ResponseEntity.status(HttpStatus.OK).body(empresaCertificadoService.findAll());
    }

    @GetMapping(value = "list-by-empresa")
    public ResponseEntity<?> listByEmpresa(@RequestParam Integer empresaId) {
        return ResponseEntity.status(HttpStatus.OK).body(empresaCertificadoService.findByEmpresaId(empresaId));
    }
}

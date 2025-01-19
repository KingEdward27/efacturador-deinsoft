/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers.web;

import com.deinsoft.efacturador3.bean.ParamBean;
import com.deinsoft.efacturador3.bean.Response;
import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.controllers.BaseController;
import com.deinsoft.efacturador3.dto.FacturaElectronicaDto;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.ResumenDiario;
import com.deinsoft.efacturador3.model.SecRoleUser;
import com.deinsoft.efacturador3.model.SecUser;
import com.deinsoft.efacturador3.security.util.AuthenticationHelper;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.ResumenDiarioService;
import com.deinsoft.efacturador3.service.SecUserService;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import com.deinsoft.efacturador3.util.ExportUtil;
import com.deinsoft.efacturador3.util.Util;
import com.deinsoft.efacturador3.util.validacion.PeriodoDetail;
import com.deinsoft.efacturador3.util.validacion.PeriodoResponse;
import com.deinsoft.efacturador3.util.validacion.SireClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author EDWARD-PC
 */
@RestController
@RequestMapping("backend")
public class FacturaBackendController extends BaseController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private FacturaElectronicaService facturaElectronicaService;

    @Autowired
    private SecUserService secUserService;

    @Autowired
    private ResumenDiarioService resumenDiarioService;

    @Autowired
    AuthenticationHelper auth;

    SireClient sireClient;

    @Autowired
    AppConfig appConfig;

    @Autowired
    EmpresaService empresaService;

    @PostMapping(value = "/listar")
    public List<FacturaElectronica> getReportActComprobante(@RequestBody ParamBean paramBean, HttpServletRequest request) {
        Map<String, Object> map = auth.getJwtLoggedUserData(request);
        String idUser = ((Map<String, Object>) map.get("user")).get("id").toString();
        paramBean.setIdUser(Long.parseLong(idUser));
        return facturaElectronicaService.getReportActComprobante(paramBean);
    }

    @RequestMapping(value = {"/xml"}, method = RequestMethod.POST)
    public ResponseEntity<?> genXml(@RequestParam(value = "id") long id) {
        Map<String, Object> resultado = null;
        try {
            resultado = this.facturaElectronicaService.generarComprobantePagoSunat(id);
            String status = resultado.get("status") == null ? "" : resultado.get("status").toString();
            String code = resultado.get("code") == null ? "" : resultado.get("code").toString();
            String message = resultado.get("message") == null ? "" : resultado.get("message").toString();
            String description = resultado.get("description") == null ? "" : resultado.get("description").toString();
            if (code.equals("-2") || code.equals("9999")) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Response(code, message, description));
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultado = new HashMap<>();
            resultado.put("code", "9999");
            resultado.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }

    @PostMapping(value = "/sunat")
    public ResponseEntity<?> sendSunat(@RequestParam(name = "id") long id) {
        Map<String, Object> resultado = null;
        resultado = facturaElectronicaService.sendToSUNAT(id);
        String status = resultado.get("status") == null ? "" : resultado.get("status").toString();
        String code = resultado.get("code") == null ? "" : resultado.get("code").toString();
        String message = resultado.get("message") == null ? "" : resultado.get("message").toString();
        String description = resultado.get("description") == null ? "" : resultado.get("description").toString();
        if (code.equals("-2") || code.equals("9999")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Response(code, message, description));
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultado);
    }

    @PostMapping(value = "/get-pdf")
    public ResponseEntity<?> getPDF(@RequestParam(name = "id") String id,
            @RequestParam(name = "tipo") int tipo,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.APPLICATION_PDF;
        byte[] data = null;

        try {
            data = facturaElectronicaService.getPDFInBtyes(new Long(id), tipo);
            if (data != null) {
                String type = "pdf";
                ByteArrayInputStream stream = new ByteArrayInputStream(data);
                if (type.equals("pdf")) {
                    headers.add("Content-Disposition", "inline; filename=pdf.pdf");
                    mediaType = MediaType.APPLICATION_PDF;
                } else if (type.equals("excel")) {
                    headers.add("Content-Disposition", "attachment; filename=excel.xlsx");
                    mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                }
                return ResponseEntity.status(HttpStatus.CREATED).headers(headers).contentType(mediaType).body(new InputStreamResource(stream));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map resultado = new HashMap<>();
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @RequestMapping(value = {"/resumendiario"}, method = RequestMethod.POST)
    public ResponseEntity<?> sendResumenDiario(
            @RequestParam(value = "ids") String ids,
            HttpServletRequest request) {
        Map<String, Object> resultado = null;
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        SecUser usuario = secUserService.getSecUserByName(auth.getName());
//
//        if (usuario == null) {
//            return "redirect : /login";
//        }
        String[] idsTrabajadores = ids.split(",");
        List<Long> listIds = new ArrayList<>();
        for (String idsTrabajadore : idsTrabajadores) {
            listIds.add(Long.valueOf(idsTrabajadore));
        }
        try {
            resultado = resumenDiarioService.generarComprobantePagoSunatFromFacturas(listIds);
            String status = resultado.get("status") == null ? "" : resultado.get("status").toString();
            String code = resultado.get("code") == null ? "" : resultado.get("code").toString();
            String message = resultado.get("message") == null ? "" : resultado.get("message").toString();
            String description = resultado.get("description") == null ? "" : resultado.get("description").toString();
            if (code.equals("-2") || code.equals("9999")) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(new Response(code, message, description));
            }
//            if(resultado != null && resultado.get("status") != null){
//                if(resultado.get("code").toString().equals("0")){
//                    model.addAttribute("success", resultado.get("status") + "\n" + resultado.get("description"));
//                }else{
//                    model.addAttribute("warning", resultado.get("status") + "\n" + " Código SUNAT rechazo: "+ resultado.get("code"));
//                }
//                
//            }else{
//                model.addAttribute("warning", resultado.get("code") + "\n" + resultado.get("message"));
//            }

        } catch (Exception e) {
            e.printStackTrace();
            resultado = new HashMap<>();
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(resultado);
            //model.addAttribute("error", resultado.get("code") + "\n" + resultado.get("message"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @GetMapping(value = "/get-empresa-by-user")
    public ResponseEntity<?> getListEmpresaByUser(@RequestParam("id") Long id, HttpServletRequest request) {
        logger.info("getSecUser received: " + id);
        List<Empresa> listSecRoleUser = secUserService.getSecRoleUserById(id).stream()
                .map(mapper -> mapper.getEmpresa())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(listSecRoleUser);
    }

    @GetMapping(value = "/get-periodos")
    public ResponseEntity<?> getPeriodos(@RequestParam("idEmpresa") int idEmpresa,
            @RequestParam("libro") String libro, HttpServletRequest request) throws Exception {
        Empresa empresa = empresaService.getEmpresaById(idEmpresa);
        sireClient = new SireClient(empresa.getSireClientId(), empresa.getSireClientSecret(),
                empresa.getNumdoc().concat(empresa.getUsuariosol()), empresa.getClavesol());
        List<PeriodoResponse> listSecRoleUser = sireClient.getPeriodos(libro);
        return ResponseEntity.status(HttpStatus.OK).body(listSecRoleUser);
    }

    @GetMapping(value = "/get-periodos-detail")
    public ResponseEntity<?> getPeriodosDetail(@RequestParam("idEmpresa") int idEmpresa, @RequestParam("anio") String anio,
            @RequestParam("libro") String libro,
            HttpServletRequest request) throws Exception {
        Empresa empresa = empresaService.getEmpresaById(idEmpresa);
        sireClient = new SireClient(empresa.getSireClientId(), empresa.getSireClientSecret(),
                empresa.getNumdoc().concat(empresa.getUsuariosol()), empresa.getClavesol());

        List<PeriodoDetail> list = sireClient.getPeriodos(libro).stream()
                .filter(predicate -> predicate.getNumEjercicio().equals(anio))
                .findFirst().orElse(new PeriodoResponse()).getLisPeriodos();

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping(value = "/gen-xml-notacredito-by-numdoc")
    public ResponseEntity<?> genXmlNotaCredito(
            @RequestParam(name = "serie") String serie,
            @RequestParam(name = "numero") String numero,
            @RequestParam(name = "empresaId") Integer empresaId,
            HttpServletRequest request, HttpServletResponse response) throws TransferirArchivoException, ParseException, JsonProcessingException {
        Map<String, Object> result = null;

        try {
            FacturaElectronica facturaElectronicaParam = new FacturaElectronica();
            facturaElectronicaParam.setSerie(serie);
            facturaElectronicaParam.setNumero(numero);
            facturaElectronicaParam.setEmpresa(empresaService.getEmpresaById(empresaId));
            result = this.facturaElectronicaService.generarNotaCredito(facturaElectronicaParam);
        } catch (Exception e) {
            e.printStackTrace();
            result = new HashMap<>();
            result.put("code", "003");
            result.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping(value = "/get-resumen-rlie")
    public ResponseEntity<?> getResumenRlie(@RequestBody ParamBean paramBean, HttpServletRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                facturaElectronicaService.getResumenRlieCombined(paramBean.getEmpresa().getId(),
                        paramBean.getPeriodo().get("perTributario"), "1", "0", paramBean.getLibro()));
    }

    @PostMapping(value = "/get-propuesta-rlie")
    public ResponseEntity<?> getPropuestaRlie(@RequestBody ParamBean paramBean, HttpServletRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                facturaElectronicaService.getPropuestaRlie(paramBean.getEmpresa().getId(),
                        paramBean.getPeriodo().get("perTributario"), "1", "0", paramBean.getLibro()));
    }

    @PostMapping(value = "/get-list-comparacion-rlie")
    public List<FacturaElectronicaDto> getComparacionRlie(@RequestBody ParamBean paramBean, HttpServletRequest request) throws Exception {
        Map<String, Object> map = auth.getJwtLoggedUserData(request);
        String idUser = ((Map<String, Object>) map.get("user")).get("id").toString();
        paramBean.setIdUser(Long.parseLong(idUser));
        return facturaElectronicaService.getReportActComprobanteCombined(paramBean);
    }
}

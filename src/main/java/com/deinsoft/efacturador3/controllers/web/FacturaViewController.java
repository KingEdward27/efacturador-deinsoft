/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers.web;

import com.deinsoft.efacturador3.controllers.BaseController;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.ResumenDiario;
import com.deinsoft.efacturador3.model.SecRoleUser;
import com.deinsoft.efacturador3.model.SecUser;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.ResumenDiarioService;
import com.deinsoft.efacturador3.service.SecUserService;
import com.deinsoft.efacturador3.util.ExportUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author EDWARD-PC
 */
@Controller
@SessionAttributes("facturaView")
public class FacturaViewController extends BaseController{

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private FacturaElectronicaService facturaElectronicaService;

    @Autowired
    private ResumenDiarioService resumenDiarioService;
    
    @Autowired
    private SecUserService secUserService;

    FacturaElectronica facturaSearch;

    public FacturaViewController(){
        facturaSearch = new FacturaElectronica();
    }
    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String index(@RequestParam(name = "page", defaultValue = "0") int page, 
            Model model,
            Authentication authentication,
            HttpServletRequest request) {
        if (authentication != null) {
            logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            logger.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado: ".concat(auth.getName()));
        }

        if (hasRole("ROLE_ADMIN")) {
            logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");

        if (securityContext.isUserInRole("ROLE_ADMIN")) {
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        if (request.isUserInRole("ROLE_ADMIN")) {
            logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        SecUser usuario = secUserService.getSecUserByName(auth.getName());

        if (usuario == null) {
            return "login";
        }
        List<Integer> listEmpresaIds = new ArrayList<>();
        for (SecRoleUser secRoleUser : usuario.getListSecRoleUser()) {
            listEmpresaIds.add(secRoleUser.getEmpresa().getId());
        }
        List<FacturaElectronica> list = new ArrayList<>();
        model.addAttribute("titulo", "Listado de Comprobantes");
        model.addAttribute("facturaSearch", this.facturaSearch);
        model.addAttribute("facturas", list);
        model.addAttribute("resumenDiario", new ResumenDiario());
        return "listar";
    }
    
    List<FacturaElectronica> verDatos(Model model,SecUser usuario){
        List<Integer> listEmpresaIds = new ArrayList<>();
        for (SecRoleUser secRoleUser : usuario.getListSecRoleUser()) {
            listEmpresaIds.add(secRoleUser.getEmpresa().getId());
        }
        List<String> estados = new ArrayList<>();
        estados.add("1");
        estados.add("2");
        estados.add("3");
        List<FacturaElectronica> list = new ArrayList<>();
        if (facturaSearch.getFechaIni() == null || !StringUtils.hasText(facturaSearch.getFechaIni()) ||
                facturaSearch.getFechaFin() == null || !StringUtils.hasText(facturaSearch.getFechaFin())) {
            if(model != null){
                model.addAttribute("titulo", "Listado de Comprobantes");
                model.addAttribute("success", "Debe ingresar la fecha inicial y final para hacer la búsqueda");
                model.addAttribute("facturas", list);
                model.addAttribute("facturaSearch", this.facturaSearch);
                model.addAttribute("resumenDiario", new ResumenDiario());
                return list;
            }
            else{
                return null;
            }
        }
//        if (facturaSearch.getFechaIni() == null || !StringUtils.hasText(facturaSearch.getFechaIni())) {
//            model.addAttribute("facturas", list);
//            model.addAttribute("facturaSearch", this.facturaSearch);
//        }
        LocalDate date1 = LocalDate.parse(this.facturaSearch.getFechaIni(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate date2 = LocalDate.parse(this.facturaSearch.getFechaFin(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        list = facturaElectronicaService.getByFechaEmisionBetweenAndEmpresaIdInAndEstadoIn(date1, date2, listEmpresaIds,estados);
        if(model != null){
            model.addAttribute("titulo", "Listado de Comprobantes");
            model.addAttribute("facturas", list);
            model.addAttribute("facturaSearch", this.facturaSearch);
            model.addAttribute("resumenDiario", new ResumenDiario());
        }
        return list;
    }
    
    @RequestMapping(value = {"/listar"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(value = "fechaIni",required = false) String fechaIni,
            @RequestParam(value = "fechaFin",required = false) String fechaFin,
            Model model,
            Authentication authentication,
            HttpServletRequest request) {
        if (authentication != null) {
            logger.info("Hola usuario autenticado, tu username es: ".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            logger.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado: ".concat(auth.getName()));
        }

        if (hasRole("ROLE_ADMIN")) {
            logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");

        if (securityContext.isUserInRole("ROLE_ADMIN")) {
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        if (request.isUserInRole("ROLE_ADMIN")) {
            logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
        } else {
            logger.info("Forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
        }

        Pageable pageRequest = PageRequest.of(page, 25);
        SecUser usuario = secUserService.getSecUserByName(auth.getName());

        if (usuario == null) {
            return "redirect : /login";
        }
        
        
        model.addAttribute("titulo", "Listado de Comprobantes");
        
        this.facturaSearch.setFechaIni(fechaIni);
        this.facturaSearch.setFechaFin(fechaFin);
        verDatos(model, usuario);
        
//        model.addAttribute("facturaSearch", this.facturaSearch);
//        model.addAttribute("facturas", list);
//        model.addAttribute("page", pageRender);
        return "listar :: lista";
    }

    @RequestMapping(value = {"/resumendiario"}, method = RequestMethod.GET)
    public String resumenDiario(Model model,
            @ModelAttribute("facturaSearch") FacturaElectronica facturaSearch,
            Authentication authentication,
            HttpServletRequest request) {
        return "listar";
    }

    @RequestMapping(value = {"/resumenbaja"}, method = RequestMethod.GET)
    public String resumenBaja(Model model,
            @ModelAttribute("facturaSearch") FacturaElectronica facturaSearch,
            Authentication authentication,
            HttpServletRequest request) {
        return "listar";
    }

    @RequestMapping(value = {"/notacredito"}, method = RequestMethod.GET)
    public String notaCredito(Model model,
            @ModelAttribute("facturaSearch") FacturaElectronica facturaSearch,
            Authentication authentication,
            HttpServletRequest request) {
        return "listar";
    }

    @RequestMapping(value = {"/factura/xml"}, method = RequestMethod.POST)
    public String genXml(@RequestParam(value = "idComprobante") long idComprobante, Model model) {
        Map<String, Object> resultado = null;
        try {
            resultado = this.facturaElectronicaService.generarComprobantePagoSunat(idComprobante);
            if(resultado != null && resultado.get("xmlHash") != null){
                model.addAttribute("success", "XML generado correctamente");
            }else{
                model.addAttribute("warning", resultado.get("code") + "\n" + resultado.get("message"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            resultado = new HashMap<>();
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            model.addAttribute("error", resultado.get("code") + "\n" + resultado.get("message"));
        }
        
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecUser usuario = secUserService.getSecUserByName(auth.getName());

        if (usuario == null) {
            return "/login";
        }
        verDatos(model, usuario);
        
//        flash.addFlashAttribute("success", resultado.get("code") + "\n" + resultado.get("message"));
        return "listar :: lista";
    }
    @RequestMapping(value = {"/factura/sunat"}, method = RequestMethod.POST)
    public String sendSunat(
            @RequestParam(value = "idComprobante") long idComprobante,
            Model model,
            RedirectAttributes flash,
            Authentication authentication,
            HttpServletRequest request){
        Map<String, Object> resultado = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecUser usuario = secUserService.getSecUserByName(auth.getName());

        if (usuario == null) {
            return "redirect : /login";
        }
        try {
            resultado = facturaElectronicaService.sendToSUNAT(idComprobante);
            if(resultado != null && resultado.get("status") != null){
                if(resultado.get("code").toString().equals("0")){
                    model.addAttribute("success", resultado.get("status") + "\n" + resultado.get("description"));
                }else{
                    model.addAttribute("warning", resultado.get("status") + "\n" + " Código SUNAT rechazo: "+ resultado.get("code"));
                }
                
            }else{
                model.addAttribute("warning", resultado.get("code") + "\n" + resultado.get("message"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            resultado = new HashMap<>();
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            model.addAttribute("error", resultado.get("code") + "\n" + resultado.get("message"));
        }
        
        verDatos(model, usuario);
        
        return "listar :: lista";
    }
    @RequestMapping(value = {"/resumendiario/send"}, method = RequestMethod.POST)
    public String sendResumenDiario( 
            @RequestParam(value = "idComprobantes") String idComprobantes,
            Model model,
            RedirectAttributes flash,
            Authentication authentication,
            HttpServletRequest request){
        Map<String, Object> resultado = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecUser usuario = secUserService.getSecUserByName(auth.getName());

        if (usuario == null) {
            return "redirect : /login";
        }
        String[] idsTrabajadores = idComprobantes.split(",");
        List<Long> listIds = new ArrayList<>();
        for (String idsTrabajadore : idsTrabajadores) {
            listIds.add(Long.valueOf(idsTrabajadore));
        }
        try {
            resultado = resumenDiarioService.generarComprobantePagoSunatFromFacturas(listIds);
            if(resultado != null && resultado.get("status") != null){
                if(resultado.get("code").toString().equals("0")){
                    model.addAttribute("success", resultado.get("status") + "\n" + resultado.get("description"));
                }else{
                    model.addAttribute("warning", resultado.get("status") + "\n" + " Código SUNAT rechazo: "+ resultado.get("code"));
                }
                
            }else{
                model.addAttribute("warning", resultado.get("code") + "\n" + resultado.get("message"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            resultado = new HashMap<>();
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            model.addAttribute("error", resultado.get("code") + "\n" + resultado.get("message"));
        }
        
        verDatos(model, usuario);
        
        return "listar :: lista";
    }
    @RequestMapping(value = {"/sendsunat"}, method = RequestMethod.GET)
    public String sendSunatMasivo(Model model,
            @ModelAttribute("facturaSearch") FacturaElectronica facturaSearch,
            Authentication authentication,
            HttpServletRequest request) {
        return "redirect:/listar";
    }
    @RequestMapping(value = {"/factura/export/excel"}, method = RequestMethod.GET)
    public String exportToExcel(Model model,HttpServletResponse response) throws IOException, IllegalArgumentException, IllegalAccessException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecUser usuario = secUserService.getSecUserByName(auth.getName());

        if (usuario == null) {
            return "redirect : /login";
        }
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=comprobantes_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        List<FacturaElectronica> list = verDatos(null, usuario);
        if(list == null){
            return "/login";
        }
        String[] arrayVisibleObjects = {"tipo","serie","numero","fechaEmision","clienteNombre","totalValorVenta","indSituacion"};
        String[] cabecera = {"Tipo","Serie","N. Documento","Fecha EmisiÓn","Cliente","Total","Situacion"};
        List<Map<String,Object>> list2= new ArrayList<>();
        for (FacturaElectronica facturaElectronica : list) {
//            Map<String, Object> map = new ObjectMapper().convertValue(facturaElectronica, Map.class);
//            list2.add(map);
            
            list2.add(facturaElectronica.toMap(facturaElectronica,arrayVisibleObjects));
        }
        ExportUtil excelExporter = new ExportUtil(list2,cabecera,arrayVisibleObjects);
         
        excelExporter.export(response);
        return null;
    }  
    //            Page<FacturaElectronica> pageList = listConvertToPage1(list, pageRequest);
//            PageRender<FacturaElectronica> pageRender = new PageRender<FacturaElectronica>("/listar", pageList);
//            model.addAttribute("page", pageRender);
   
    @RequestMapping(value = {"/factura/view-detail"}, method = RequestMethod.POST)
    public String viewDetailFactura(@RequestParam(value = "idComprobante") long idComprobante, Model model) {
        Map<String, Object> resultado = null;
        try {
            ResumenDiario rd = this.resumenDiarioService.getResumenByDoc(idComprobante);
            if (rd == null) rd = new ResumenDiario();
            model.addAttribute("resumenDiario",rd);
        } catch (Exception e) {
            e.printStackTrace();
            resultado = new HashMap<>();
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            model.addAttribute("error", resultado.get("code") + "\n" + resultado.get("message"));
        }
        
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecUser usuario = secUserService.getSecUserByName(auth.getName());

        if (usuario == null) {
            return "/login";
        }
        return "listar :: dDetailForm";
    }
    @RequestMapping(value = {"/factura/see-ticket"}, method = RequestMethod.POST)
    public String consultarTicket(@RequestParam(value = "idResumen") Long idResumen, Model model) {
        Map<String, Object> resultado = null;
        try {
            ResumenDiario rd = resumenDiarioService.consultarTicketSunat("", idResumen);
            if (rd == null) rd = new ResumenDiario();
            model.addAttribute("resumenDiario",rd);
        } catch (Exception e) {
            e.printStackTrace();
            resultado = new HashMap<>();
            resultado.put("code", "003");
            resultado.put("message", e.getMessage());
            model.addAttribute("error", resultado.get("code") + "\n" + resultado.get("message"));
        }
        
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecUser usuario = secUserService.getSecUserByName(auth.getName());

        if (usuario == null) {
            return "/login";
        }
        return "listar :: dDetailForm";
    }
    public FacturaElectronica getFacturaSearch() {
        return facturaSearch;
    }

    public void setFacturaSearch(FacturaElectronica facturaSearch) {
        this.facturaSearch = facturaSearch;
    }

}

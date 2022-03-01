/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ComprobanteDet;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.commons.controllers.CommonController;
import com.deinsoft.efacturador3.commons.service.CommonService;
import com.deinsoft.efacturador3.commons.service.CommonServiceImpl;
//import com.deinsoft.efacturador3.model.Documento;
import com.deinsoft.efacturador3.service.BandejaDocumentosService;
import com.deinsoft.efacturador3.service.ComunesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.deinsoft.efacturador3.repository.FacturaElectronicaRepository;
import com.deinsoft.efacturador3.service.EmpresaService;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 *
 * @author EDWARD-PC
 */
@RestController
@RequestMapping("document")
public class FacturaController {

    private static final Logger log = LoggerFactory.getLogger(FacturaController.class);

    @Autowired
    private BandejaDocumentosService bandejaDocumentosService;


    @Autowired
    FacturaElectronicaService facturaElectronicaService;

    @Autowired
    EmpresaService empresaService;
    
    @PostMapping(value = "/send-document")
    public ResponseEntity<?> sendDocument(@Valid @RequestBody ComprobanteCab documento, BindingResult result) throws TransferirArchivoException, ParseException {
        if (result.hasErrors()) {
            return this.validar(result);
        }
        BigDecimal totalValorVentasGravadas = BigDecimal.ZERO, totalValorVentasInafectas = BigDecimal.ZERO,
                totalValorVentasExoneradas = BigDecimal.ZERO,
                SumatoriaIGV = BigDecimal.ZERO, SumatoriaISC = BigDecimal.ZERO,
                sumatoriaOtrosTributos = BigDecimal.ZERO, sumatoriaOtrosCargos = BigDecimal.ZERO, totalValorVenta = BigDecimal.ZERO;
        for (ComprobanteDet detalle : documento.getLista()) {
            if (Integer.valueOf(detalle.getTipo_igv()) >= 10 && Integer.valueOf(detalle.getTipo_igv()) <= 20) {
                totalValorVentasGravadas = totalValorVentasGravadas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            } else if (Integer.valueOf(detalle.getTipo_igv()) >= 30 && Integer.valueOf(detalle.getTipo_igv()) <= 36) {
                totalValorVentasInafectas = totalValorVentasInafectas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            } else {
                totalValorVentasExoneradas = totalValorVentasExoneradas.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
            }
            SumatoriaIGV = SumatoriaIGV.add(detalle.getAfectacionIGV());
            SumatoriaISC = SumatoriaISC.add(detalle.getAfectacionISC() == null ? BigDecimal.ZERO : detalle.getAfectacionISC());
            totalValorVenta = totalValorVenta.add(detalle.getCantidad().multiply(detalle.getPrecio_unitario()));
        }
        documento.setSumatoriaIGV(SumatoriaIGV);
        documento.setSumatoriaISC(SumatoriaISC);
        documento.setSumatoriaOtrosCargos(BigDecimal.ZERO);
        documento.setSumatoriaOtrosTributos(BigDecimal.ZERO);
        documento.setTotalValorVentasGravadas(totalValorVentasGravadas);
        documento.setTotalValorVentasInafectas(totalValorVentasInafectas);
        documento.setTotalValorVentasExoneradas(totalValorVentasExoneradas);

        FacturaElectronica comprobante = new FacturaElectronica();
        comprobante.setTipo(documento.getTipo());
        comprobante.setTipoOperacion(documento.getTipo_operacion());
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(documento.getFecha_emision());
//	java.sql.Date dateSql = new java.sql.Date(date1.getTime());
        comprobante.setFechaEmision(date1);
        comprobante.setSerie(documento.getSerie());
        comprobante.setNumero(documento.getNumero());
        comprobante.setFechaVencimiento(documento.getFecha_vencimiento());
        comprobante.setClienteTipo(documento.getCliente_tipo());
        comprobante.setClienteNombre(documento.getCliente_nombre());
        comprobante.setClienteDocumento(documento.getCliente_documento());
        comprobante.setClienteDireccion(documento.getCliente_direccion());
        comprobante.setClienteEmail(documento.getCliente_email());
        comprobante.setClienteTelefono(documento.getCliente_telefono());
        comprobante.setVendedorNombre(documento.getVendedor_nombre());
        comprobante.setObservaciones(documento.getObservaciones());
        comprobante.setPlacaVehiculo(documento.getPlaca_vehiculo());
        comprobante.setOrdenCompra(documento.getOrden_compra());
        comprobante.setGuiaRemision(documento.getGuia_remision());
        comprobante.setDescuentoGlobalPorcentaje(documento.getDescuento_global_porcentaje());
        comprobante.setMoneda(documento.getMoneda());
        comprobante.setNotaTipo(documento.getNota_tipo());
        comprobante.setNotaMotivo(documento.getNota_motivo());
        comprobante.setNotaReferenciaTipo(documento.getNota_referencia_tipo());
        comprobante.setNotaReferenciaSerie(documento.getNota_referencia_serie());
        comprobante.setNotaReferenciaNumero(documento.getNota_referencia_numero());
        comprobante.setIncluirPdf(documento.getIncluir_pdf());
        comprobante.setIncluirXml(documento.getIncluir_xml());
        comprobante.setSumatoriaIGV(SumatoriaIGV);
        comprobante.setSumatoriaISC(SumatoriaISC);
        comprobante.setSumatoriaOtrosCargos(BigDecimal.ZERO);
        comprobante.setSumatoriaOtrosTributos(BigDecimal.ZERO);
        comprobante.setTotalValorVentasGravadas(totalValorVentasGravadas);
        comprobante.setTotalValorVentasInafectas(totalValorVentasInafectas);
        comprobante.setTotalValorVentasExoneradas(totalValorVentasExoneradas);
        comprobante.setTotalValorVenta(totalValorVenta);
        comprobante.setCodLocal("0000");
        comprobante.setFormaPago(documento.getForma_pago());
        List<FacturaElectronicaDet> list = new ArrayList<>();
        List<FacturaElectronicaTax> listTax = new ArrayList<>();
        BigDecimal baseamt = BigDecimal.ZERO, taxtotal = BigDecimal.ZERO;
        for (ComprobanteDet comprobanteDet : documento.getLista()) {
            FacturaElectronicaDet det = new FacturaElectronicaDet();
            det.setDescripcion(comprobanteDet.getDescripcion());
            det.setUnidadMedida(comprobanteDet.getUnidad_medida());
            det.setCantidad(comprobanteDet.getCantidad());
            det.setPrecioVentaUnitario(comprobanteDet.getPrecio_unitario());
            det.setValorVentaItem(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacionIGV()));
            det.setValorUnitario(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacionIGV()));
            det.setAfectacionIgv(comprobanteDet.getAfectacionIGV());
            det.setAfectacionIGVCode(comprobanteDet.getTipo_igv());
            det.setDescuento(comprobanteDet.getDescuento_porcentaje().
                    divide(new BigDecimal(100)).
                    multiply(comprobanteDet.getCantidad().multiply(comprobanteDet.getPrecio_unitario())));
            det.setRecargo(comprobanteDet.getRecargo());
            list.add(det);
            
            baseamt = baseamt.add(comprobanteDet.getPrecio_unitario().subtract(comprobanteDet.getAfectacionIGV()));
            taxtotal = taxtotal.add(comprobanteDet.getAfectacionIGV());
            FacturaElectronicaTax facturaElectronicaTax = new FacturaElectronicaTax();
            facturaElectronicaTax.setTaxId(1000);
            facturaElectronicaTax.setBaseamt(baseamt);
            facturaElectronicaTax.setTaxtotal(taxtotal);
            listTax.add(facturaElectronicaTax);
        }
        comprobante.setListFacturaElectronicaDet(list);
        comprobante.setListFacturaElectronicaTax(listTax);
        comprobante.getListFacturaElectronicaDet().stream().forEach(item -> {
            comprobante.addFacturaElectronicaDet(item);
        });
        comprobante.getListFacturaElectronicaTax().stream().forEach(item -> {
            comprobante.addFacturaElectronicaTax(item);
        });
        comprobante.setIndSituacion("01");
        
        Empresa empresa = empresaService.getEmpresaById(9);
        comprobante.setEmpresa(empresa);
        
        List<String> listDocIds = Arrays.asList("0","1","4","6","7","A");
        if(!listDocIds.contains(comprobante.getClienteTipo())){
            return ResponseEntity.status(HttpStatus.FOUND).body("El tipo de documento de identidad no existe");
        }
        
        List<FacturaElectronica> listFact = facturaElectronicaService.getBySerieAndNumero(comprobante);
        if(listFact == null || (listFact != null && listFact.size() > 0)){
            return ResponseEntity.status(HttpStatus.FOUND).body("El documento ya existe");
        }
        
        if(comprobante.getClienteTipo().equals("1") 
                && String.format("%02d", Integer.parseInt(comprobante.getTipo())).equals("01")){
            return ResponseEntity.status(HttpStatus.FOUND).body("El dato ingresado en el tipo de documento de identidad del receptor no esta permitido para el tipo de comprobante");
        }
        if(comprobante.getClienteTipo().equals("1") && comprobante.getClienteDocumento().length() != 8 || 
                comprobante.getClienteTipo().equals("6") && comprobante.getClienteDocumento().length() != 11){
            return ResponseEntity.status(HttpStatus.FOUND).body("El número de documento del cliente no cumple con el tamaño requerido para el tipo de comprobante");
        }
        if(!comprobante.getFormaPago().equals("Contado") && !comprobante.getFormaPago().equals("Credito")){
            return ResponseEntity.status(HttpStatus.FOUND).body("El campo forma de pago solo acepta los valores Contado/Credito");
        }
        FacturaElectronica facturaElectronicaResult = facturaElectronicaService.save(comprobante);
        
        String rootpath = "D://DEFACT/";
        String docGene = this.bandejaDocumentosService.generarComprobantePagoSunat(rootpath,empresa,facturaElectronicaResult);
        log.info("docGene: " + docGene);
        if ("".equals(docGene)) {
            facturaElectronicaResult.setFechaGenXml(new Date());
            facturaElectronicaResult.setIndSituacion("02");
            facturaElectronicaResult = facturaElectronicaService.save(facturaElectronicaResult);
//            listadoBandeja = this.bandejaDocumentosService.consultarBandejaComprobantes();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaElectronicaResult);
    }
    @PostMapping(value = "/send-sunat")
    public ResponseEntity<?> enviarXML(@RequestParam(name = "id") String id) throws Exception {
        log.debug("SoftwareFacturadorController.enviarXML...Iniciando el procesamiento");
        HashMap<String, Object> resultado = new HashMap<>();
        String retorno = "", mensajeValidacion = "", resultadoProceso = "EXITO";
        String rootPath = "D:/DEFACT/";
        FacturaElectronica facturaElectronica = facturaElectronicaService.getById(Long.parseLong(id));
        try {
            log.debug("SoftwareFacturadorController.enviarXML...Consultar Comprobante");

            log.debug("SoftwareFacturadorController.enviarXML...Enviar Comprobante");
            HashMap<String, Object> envioBandeja = this.bandejaDocumentosService.enviarComprobantePagoSunat(rootPath, facturaElectronica);
            if (envioBandeja != null) {

                HashMap<String, Object> resultadoWebService = (HashMap<String, Object>) envioBandeja.get("resultadoWebService");

                String estadoRetorno = (resultadoWebService.get("situacion") != null) ? (String) resultadoWebService.get("situacion") : "";

                String mensaje = (resultadoWebService.get("mensaje") != null) ? (String) resultadoWebService.get("mensaje") : "-";
                if (!"".equals(estadoRetorno)) {
                    if (!"11".equals(estadoRetorno)
                            && !"12".equals(estadoRetorno)) {
//                                docResp.setFecEnvi("FECHA_ENVIO");
//                                docResp.setIndSitu(estadoRetorno);
//                                docResp.setDesObse(mensaje);
                        facturaElectronica.setFechaEnvio(new Date());
                        facturaElectronica.setIndSituacion(estadoRetorno);
                        facturaElectronica.setObservacionEnvio(mensaje);
                        facturaElectronicaService.save(facturaElectronica);
                    } else {
//                                docResp.setFecEnvi("CDR");
//                                docResp.setIndSitu(estadoRetorno);
//                                docResp.setDesObse(mensaje);
                        facturaElectronica.setIndSituacion(estadoRetorno);
                        facturaElectronica.setObservacionEnvio(mensaje);
                        facturaElectronicaService.save(facturaElectronica);
                    }
                }
                mensajeValidacion = mensaje;
                resultadoProceso = estadoRetorno;
            }
//                List<Documento> lista = this.bandejaDocumentosService.consultarBandejaComprobantesPorId(documento);
//                if (lista.size() > 0) {
//                    docResp = lista.get(0);
//                    log.debug("SoftwareFacturadorController.enviarXML...Enviar Comprobante");
//                    HashMap<String, Object> envioBandeja = this.bandejaDocumentosService.enviarComprobantePagoSunat(docResp);
//                    if (envioBandeja != null) {
//
//                        HashMap<String, Object> resultadoWebService = (HashMap<String, Object>) envioBandeja.get("resultadoWebService");
//
//                        String estadoRetorno = (resultadoWebService.get("situacion") != null) ? (String) resultadoWebService.get("situacion") : "";
//
//                        String mensaje = (resultadoWebService.get("mensaje") != null) ? (String) resultadoWebService.get("mensaje") : "-";
//                        if (!"".equals(estadoRetorno)) {
//                            if (!"11".equals(estadoRetorno)
//                                    && !"12".equals(estadoRetorno)) {
//                                docResp.setFecEnvi("FECHA_ENVIO");
//                                docResp.setIndSitu(estadoRetorno);
//                                docResp.setDesObse(mensaje);
//                                this.bandejaDocumentosService.actualizarEstadoBandejaCdp(docResp);
//                            } else {
//                                docResp.setFecEnvi("CDR");
//                                docResp.setIndSitu(estadoRetorno);
//                                docResp.setDesObse(mensaje);
//                                this.bandejaDocumentosService.actualizarEstadoBandejaCdp(docResp);
//                            }
//                        }
//                    }
//                } else {
//                    mensajeValidacion = "No existen datos que procesar.";
//                    resultadoProceso = "FALLO";
//                }

        } catch (Exception e) {
            String mensaje = "Hubo un problema al invocar servicio SUNAT: " + e.getMessage();
            e.printStackTrace();
            log.error(mensaje);
//                docResp.setIndSitu("06");
//                docResp.setDesObse(mensaje);
            facturaElectronica.setFechaEnvio(new Date());
            facturaElectronica.setIndSituacion("06");
            facturaElectronica.setObservacionEnvio(mensaje);
            facturaElectronicaService.save(facturaElectronica);
//                this.bandejaDocumentosService.actualizarEstadoBandejaCdp(docResp);
        }

//            listadoBandeja = this.bandejaDocumentosService.consultarBandejaComprobantes();
//            resultado.put("listaBandejaFacturador", listadoBandeja);
            resultado.put("mensaje", mensajeValidacion);
            resultado.put("codigo", resultadoProceso);
//        } else {
//
//            if (!versionActualizada.booleanValue()) {
//                mensajeValidacion = "La aplicación S.F.S. se encuentra desactualizada.";
//            }
//
//            listadoBandeja = new ArrayList<>();
//
//            resultado.put("listaBandejaFacturador", listadoBandeja);
//            resultado.put("mensaje", mensajeValidacion);
//            resultado.put("validacion", resultadoProceso);
//        }
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            retorno = mapper.writeValueAsString(resultado);
//        } catch (Exception e) {
//            mensajeValidacion = e.getMessage();
//            resultadoProceso = "FALLO";
//            resultado.put("mensaje", mensajeValidacion);
//            resultado.put("validacion", resultadoProceso);
//            log.error(mensajeValidacion);
//        }
        log.debug("SoftwareFacturadorController.enviarXML...Terminando el procesamiento");

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
    protected ResponseEntity<?> validar(BindingResult result) {
        Map<String, Object> errores = new HashMap<>();
//		final List<String> errors = result.getAllErrors().stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), " El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errores);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.model.Documento;
import com.deinsoft.efacturador3.service.BandejaDocumentosService;
import com.deinsoft.efacturador3.service.ComunesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author EDWARD-PC
 */
@RestController
public class SendDocumentController {

    private static final Logger log = LoggerFactory.getLogger(SendDocumentController.class);

    public static final String UPDATE_PATH = "/enviarXML.htm";

    @Autowired
    private BandejaDocumentosService bandejaDocumentosService;

    @Autowired
    private ComunesService comunesService;

//    public ResponseEntity<?> enviarXML(@RequestBody Documento documento) throws Exception {
//        log.debug("SoftwareFacturadorController.enviarXML...Iniciando el procesamiento");
//        HashMap<String, Object> resultado = new HashMap<>();
////    URI uri = UriHelper.getUriForId(uriInfo, Integer.valueOf(12));
//        String retorno = "", mensajeValidacion = "", resultadoProceso = "EXITO";
//        List<Documento> listadoBandeja = new ArrayList<>();
//
//        log.debug("Validar que Parametros esten Registrados");
//        mensajeValidacion = this.bandejaDocumentosService.validarParametroRegistrado();
//
//        log.debug("Validar que la versión es la última publicada");
//        Boolean versionActualizada = this.comunesService.validarVersionFacturador("1.3.2");
//
//        if ("".equals(mensajeValidacion) && versionActualizada.booleanValue()) {
//
//            Documento docResp = null;
//
//            try {
//                log.debug("SoftwareFacturadorController.enviarXML...Consultar Comprobante");
//                List<Documento> lista = this.bandejaDocumentosService.consultarBandejaComprobantesPorId(documento);
//
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
//
//            } catch (Exception e) {
//                String mensaje = "Hubo un problema al invocar servicio SUNAT: " + e.getMessage();
//                log.error(mensaje);
//                docResp.setIndSitu("06");
//                docResp.setDesObse(mensaje);
//                this.bandejaDocumentosService.actualizarEstadoBandejaCdp(docResp);
//            }
//
//            listadoBandeja = this.bandejaDocumentosService.consultarBandejaComprobantes();
//            resultado.put("listaBandejaFacturador", listadoBandeja);
//            resultado.put("mensaje", mensajeValidacion);
//            resultado.put("validacion", resultadoProceso);
//
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
//
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
//
//        log.debug("SoftwareFacturadorController.enviarXML...Terminando el procesamiento");
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
//    }
}

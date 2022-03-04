/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.ComprobanteCuotas;
import com.deinsoft.efacturador3.bean.ComprobanteDet;
import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.FacturaElectronicaDet;
import com.deinsoft.efacturador3.model.FacturaElectronicaTax;
import com.deinsoft.efacturador3.commons.controllers.CommonController;
import com.deinsoft.efacturador3.commons.service.CommonService;
import com.deinsoft.efacturador3.commons.service.CommonServiceImpl;
import com.deinsoft.efacturador3.model.FacturaElectronicaCuotas;
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
import com.deinsoft.efacturador3.util.Constantes;
import com.deinsoft.efacturador3.util.FacturadorUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
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
        FacturaElectronica facturaElectronicaResult = null;
        try {
            if (result.hasErrors()) {
                return this.validar(result);
            }
            String mensajeValidacion = validarComprobante(documento);
            if(!mensajeValidacion.equals("")){
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(mensajeValidacion);
            }
            BigDecimal totalValorVentasGravadas = BigDecimal.ZERO, totalValorVentasInafectas = BigDecimal.ZERO,
                    totalValorVentasExoneradas = BigDecimal.ZERO,
                    SumatoriaIGV = BigDecimal.ZERO, SumatoriaISC = BigDecimal.ZERO,
                    sumatoriaOtrosTributos = BigDecimal.ZERO, sumatoriaOtrosCargos = BigDecimal.ZERO, totalValorVenta = BigDecimal.ZERO;
            //constantes o bd
            for (ComprobanteDet detalle : documento.getLista_productos()) {
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
            comprobante.setPorcentajeIGV(new BigDecimal(Constantes.PORCENTAJE_IGV));
            comprobante.setMontoNetoPendiente(documento.getMonto_neto_pendiente());
            comprobante.setTipoMonedaMontoNetoPendiente(documento.getMoneda_monto_neto_pendiente());
            List<FacturaElectronicaDet> list = new ArrayList<>();
            List<FacturaElectronicaTax> listTax = new ArrayList<>();
            List<FacturaElectronicaCuotas> listCuotas = new ArrayList<>();
            BigDecimal baseamt = BigDecimal.ZERO, taxtotal = BigDecimal.ZERO;

            for (ComprobanteDet comprobanteDet : documento.getLista_productos()) {
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

            if(!CollectionUtils.isEmpty(documento.getLista_cuotas())){

                BigDecimal sumaCoutas = BigDecimal.ZERO;
                for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {
                    FacturaElectronicaCuotas det = new FacturaElectronicaCuotas();
                    det.setMtoCuotaPago(detalle.getMonto_pago());
                    det.setTipMonedaCuotaPago(detalle.getTipo_moneda_pago());
                    det.setFecCuotaPago(new SimpleDateFormat("dd/MM/yyyy").parse(detalle.getFecha_pago()));
                    listCuotas.add(det);
                }

            }

            comprobante.setListFacturaElectronicaDet(list);
            comprobante.setListFacturaElectronicaTax(listTax);
            comprobante.setListFacturaElectronicaCuotas(listCuotas);

            comprobante.getListFacturaElectronicaDet().stream().forEach(item -> {
                comprobante.addFacturaElectronicaDet(item);
            });
            comprobante.getListFacturaElectronicaTax().stream().forEach(item -> {
                comprobante.addFacturaElectronicaTax(item);
            });
            comprobante.getListFacturaElectronicaCuotas().stream().forEach(item -> {
                comprobante.addFacturaElectronicaCuotas(item);
            });
            //constantes o bd
            comprobante.setIndSituacion("01");

            Empresa empresa = empresaService.getEmpresaById(9);
            comprobante.setEmpresa(empresa);

            List<FacturaElectronica> listFact = facturaElectronicaService.getBySerieAndNumero(comprobante);
            if(listFact == null || (listFact != null && listFact.size() > 0)){
                return ResponseEntity.status(HttpStatus.FOUND).body("El documento ya existe");
            }

            facturaElectronicaResult = facturaElectronicaService.save(comprobante);

            //constantes o bd
            String rootpath = "D://DEFACT/";
            String docGene = this.bandejaDocumentosService.generarComprobantePagoSunat(rootpath,empresa,facturaElectronicaResult);

            if ("".equals(docGene)) {
                facturaElectronicaResult.setFechaGenXml(new Date());
                facturaElectronicaResult.setIndSituacion("02");
                facturaElectronicaResult = facturaElectronicaService.save(facturaElectronicaResult);
            }
            log.info("docGene: " + facturaElectronicaResult.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocurrió un error inesperado: "+e.getMessage());
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
    protected String validarComprobante(ComprobanteCab documento) {
        if(documento.getCliente_tipo().equals("1") 
                && String.format("%02d", Integer.parseInt(documento.getTipo())).equals("01")){
            return "El dato ingresado en el tipo de documento de identidad del receptor no esta permitido para el tipo de comprobante";
        }
        if(documento.getCliente_tipo().equals("1") && documento.getCliente_documento().length() != 8 || 
                documento.getCliente_tipo().equals("6") && documento.getCliente_documento().length() != 11){
            return "El número de documento del cliente no cumple con el tamaño requerido para el tipo de comprobante";
        }
        if(CollectionUtils.isEmpty(documento.getLista_productos())){
            return "Debe indicar el detalle de productos del comprobante, campo: lista_productos";
        }
        if(!documento.getForma_pago().equals(Constantes.FORMA_PAGO_CONTADO) && !documento.getForma_pago().equals(Constantes.FORMA_PAGO_CREDITO)){
            return "El campo forma de pago solo acepta los valores Contado/Credito";
        }
        if(documento.getForma_pago().equals(Constantes.FORMA_PAGO_CREDITO) && CollectionUtils.isEmpty(documento.getLista_cuotas())){
            return "Si la forma de pago es Credito debe indicar al menos una cuota, campo: lista_cuotas";
        }
        if(documento.getForma_pago().equals(Constantes.FORMA_PAGO_CREDITO) && 
                FacturadorUtil.isNullOrEmpty(documento.getMonto_neto_pendiente())){
            return "Si la forma de pago es Credito debe indicar el monto neto pendiente de pago";
        }
        //1. cambiar por clase catalogos
        //2. externalizar archivo
        List<String> listDocIds = Arrays.asList("0","1","4","6","7","A");
        if(!listDocIds.contains(documento.getCliente_tipo())){
            return "El tipo de documento de identidad no existe";
        }
        if(documento.getForma_pago().equals(Constantes.FORMA_PAGO_CONTADO) && !CollectionUtils.isEmpty(documento.getLista_cuotas())){
            return "Si la forma de pago es Contado no es necesario indicar la lista de cuotas, campo: lista_cuotas";
        }
        for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {
                Date fechaPago = null;
                try {
                    fechaPago = new SimpleDateFormat("dd/MM/yyyy").parse(detalle.getFecha_pago());
                } catch (Exception e) {
                    return "Si la forma de pago es Credito la fecha de pago no debe estar vacía y debe tener formato correcto dd/MM/yyyy, campo: fecha_pago";
                }
                if(FacturadorUtil.isNullOrEmpty(detalle.getMonto_pago())){
                    return "Si la forma de pago es Credito debe indicar al monto de la cuota, campo: monto_pago";
                }
                if(FacturadorUtil.isNullOrEmpty(detalle.getTipo_moneda_pago())){
                    return "Si la forma de pago es Credito debe indicar el tipo de moneda de la cuota, campo: tipo_moneda_pago";
                }
        }
        if(!CollectionUtils.isEmpty(documento.getLista_cuotas())){
            
            BigDecimal sumaCoutas = BigDecimal.ZERO;
            for (ComprobanteCuotas detalle : documento.getLista_cuotas()) {
                
                
                sumaCoutas = sumaCoutas.add(detalle.getMonto_pago());
                
            }
            if(sumaCoutas.compareTo(documento.getMonto_neto_pendiente()) != 0){
                    return "La suma de las cuotas debe ser igual al Monto neto pendiente de pago";

                }
        }
        return "";
    }
}

package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import com.deinsoft.efacturador3.model.Documento;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;

public interface BandejaDocumentosService {
//  void eliminarBandeja(Documento paramDocumento) throws Exception;
  
//  List<Documento> consultarBandejaComprobantesPorId(Documento paramDocumento);
  
//  List<Documento> consultarBandejaComprobantes() throws Exception;
  
//  void cargarArchivosContribuyente() throws Exception;
  
  String generarComprobantePagoSunat(String rootpath,Empresa empresa,FacturaElectronica paramDocumento) throws TransferirArchivoException;
  
  HashMap<String, Object> listarCertificados() throws Exception;
  
//  HashMap<String, Object> obtenerParametros() throws Exception;
  
////  HashMap<String, String> grabarParametro(HashMap<String, Object> paramHashMap) throws Exception;
  
//  List<Documento> buscarBandejaPorSituacion(String paramString) throws Exception;
  
//  void actualizarEstadoBandejaCdp(Documento paramDocumento);
  
//  HashMap<String, String> grabarOtrosParametros(HashMap<String, Object> paramHashMap) throws Exception;
  
//  Map<String, Object> obtenerOtrosParametros() throws Exception;
  
//  String validarParametroRegistrado() throws Exception;
  
//  void cargarArchivoContribuyente() throws Exception;
//  
//  void cargarArchivoContribuyente(String paramString1, String paramString2);
}

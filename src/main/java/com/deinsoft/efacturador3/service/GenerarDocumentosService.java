package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.FacturaElectronica;
import java.util.HashMap;
import java.util.Map;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;

public interface GenerarDocumentosService {
//  InputStream formatoPlantillaXml(Empresa empresa,FacturaElectronica facturaElectronica) throws TransferirArchivoException;
  public void formatoPlantillaXml(String rootPath, FacturaElectronica facturaElectronica,String nombreArchivo) throws TransferirArchivoException;
//  String firmarComprimirXml(String paramString);
  
//  public String firmarXml(String rootPath,FacturaElectronica facturaElectronica, String nombreArchivo);
//  
//  ByteArrayOutputStream firmarXml(String path,String nomFile,Empresa empresa, InputStream inputStream);
  public String firmarXml(String rootPath,FacturaElectronica facturaElectronica, String nombreArchivo);
  
  HashMap<String, String> enviarArchivoSunat(String paramString1,String rootPath, String paramString2, FacturaElectronica paramString3);
  
//  void formatoJsonPlantilla(Documento paramDocumento) throws TransferirArchivoException;
  
  Map<String, String> obtenerEstadoTicket(String paramString1, String paramString2, String paramString3);
  
  String obtenerArchivoXml(String paramString);
  
  void validarPlazo(String paramString);
}
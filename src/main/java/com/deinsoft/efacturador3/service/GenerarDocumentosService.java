package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.Empresa;
import com.deinsoft.efacturador3.model.FacturaElectronica;
import com.deinsoft.efacturador3.model.ResumenBaja;
import com.deinsoft.efacturador3.model.ResumenDiario;
import java.util.HashMap;
import java.util.Map;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import java.util.List;

public interface GenerarDocumentosService {
//  InputStream formatoPlantillaXml(Empresa empresa,FacturaElectronica facturaElectronica) throws TransferirArchivoException;
  public byte[] formatoPlantillaXml(String rootPath, FacturaElectronica facturaElectronica,String nombreArchivo) throws TransferirArchivoException;
  public byte[] formatoPlantillaXmlResumenBaja(String rootPath, ResumenBaja resumenBaja) throws TransferirArchivoException;
  public byte[] formatoPlantillaXmlResumenDiario(String rootPath, ResumenDiario resumenDiario) throws TransferirArchivoException;
//  String firmarComprimirXml(String paramString);
  
//  public String firmarXml(String rootPath,FacturaElectronica facturaElectronica, String nombreArchivo);
//  
//  ByteArrayOutputStream firmarXml(String path,String nomFile,Empresa empresa, InputStream inputStream);
  public Map<String,Object> firmarXml(String rootPath,Empresa empresa, String nombreArchivo);
  
//  BillServiceModel enviarArchivoSunat(String paramString1,String rootPath, String paramString2, FacturaElectronica paramString3);
  
//  void formatoJsonPlantilla(Documento paramDocumento) throws TransferirArchivoException;
  
//  Map<String, String> obtenerEstadoTicket(String paramString1, String paramString2, String paramString3);
  
  String obtenerArchivoXml(String paramString);
  
  void validarPlazo(String paramString);
}
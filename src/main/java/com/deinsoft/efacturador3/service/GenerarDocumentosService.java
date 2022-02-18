package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.bean.Document;
import com.deinsoft.efacturador3.bean.FacturaElectronica;
import com.deinsoft.efacturador3.model.Documento;
import java.util.HashMap;
import java.util.Map;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public interface GenerarDocumentosService {
  InputStream formatoPlantillaXml(FacturaElectronica facturaElectronica) throws TransferirArchivoException;
  
  String firmarComprimirXml(String paramString);
  
  ByteArrayOutputStream firmarXml(InputStream inputStream);
  
  String Desencriptar(String paramString);
  
  String Encriptar(String paramString);
  
  HashMap<String, String> enviarArchivoSunat(String paramString1, String paramString2, String paramString3);
  
  void formatoJsonPlantilla(Documento paramDocumento) throws TransferirArchivoException;
  
  Map<String, String> obtenerEstadoTicket(String paramString1, String paramString2, String paramString3);
  
  String obtenerArchivoXml(String paramString);
  
  void adicionarInformacionFacturador(String paramString);
  
  void validarPlazo(String paramString);
}
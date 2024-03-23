package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.model.Empresa;
import java.util.List;
import java.util.Properties;
import com.deinsoft.efacturador3.model.Parametro;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;

public interface ComunesService {
  String obtenerRutaTrabajo(String paramString);
  
  boolean validarConexion(String paramString, Integer paramInteger);
  
  Boolean validarVersionFacturador(String paramString);
  
  Boolean actualizarVersionFacturador(String paramString);
  
  String leerEtiquetaArchivoXml(String paramString1, String paramString2);
  
  List<String> leerEtiquetaListaArchivoXml(String paramString1, String paramString2);
  
//  Documento_ consultarBandejaPorNomArch(Documento_ paramDocumento);
  
  Parametro obtenerParametro(Parametro paramParametro);
  
  Properties getProperties(String paramString);
  public BillServiceModel enviarArchivoSunat(String wsUrl, String rootPath, String filename, Empresa Empresa)  throws Exception;
  
  public BillServiceModel consultarTicketSUNAT(String wsUrl, String rootPath,String ticket, Empresa empresa);
}


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\sistema\facturador\service\ComunesService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
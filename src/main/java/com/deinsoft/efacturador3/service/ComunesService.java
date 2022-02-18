package com.deinsoft.efacturador3.service;

import java.util.List;
import java.util.Properties;
import com.deinsoft.efacturador3.model.Documento;
import com.deinsoft.efacturador3.model.Parametro;

public interface ComunesService {
  String obtenerRutaTrabajo(String paramString);
  
  void validarConexion(String paramString, Integer paramInteger);
  
  Boolean validarVersionFacturador(String paramString);
  
  Boolean actualizarVersionFacturador(String paramString);
  
  String leerEtiquetaArchivoXml(String paramString1, String paramString2);
  
  List<String> leerEtiquetaListaArchivoXml(String paramString1, String paramString2);
  
//  Documento_ consultarBandejaPorNomArch(Documento_ paramDocumento);
  
  Parametro obtenerParametro(Parametro paramParametro);
  
  Properties getProperties(String paramString);
}


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\sistema\facturador\service\ComunesService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
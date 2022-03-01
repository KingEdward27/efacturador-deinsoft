package com.deinsoft.efacturador3.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class ResourceResolver implements LSResourceResolver {
  private static final Logger log = LoggerFactory.getLogger(ResourceResolver.class);


  
  public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, 
          String baseURI){
    String rutaArchivo = "";
    
    rutaArchivo = systemId.replace("../", "");
    log.debug("rutaArchivo Primera Depuracion: " + rutaArchivo);
    
    if (rutaArchivo.indexOf("common/") == -1) {
      rutaArchivo = "common/" + systemId;
    }
    log.debug("rutaArchivo Segunda Depuracion: " + rutaArchivo);
    InputStream resourceAsStream = null;
      try {
          resourceAsStream = new FileInputStream("D:/SFS_v1.3.2/sunat_archivos/sfs/VALI/commons/xsd/2.1/"+ rutaArchivo);
      } catch (Exception e) {
          e.printStackTrace();
      }
    return new Input(publicId, rutaArchivo, resourceAsStream);
  }
}
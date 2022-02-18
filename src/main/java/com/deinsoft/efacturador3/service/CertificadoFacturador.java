package com.deinsoft.efacturador3.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import org.jboss.weld.exceptions.IllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertificadoFacturador {
  private static final Logger log = LoggerFactory.getLogger(CertificadoFacturador.class);

  
  public String validaCertificado(String input, String numRuc, String passPrivateKey) {
    FileInputStream fis;
    log.debug("GenerarDocumentosServiceImpl.validaCertificado...Iniciando validacion de certificado");
    String validacion = "", alias = "", cadena = "";
    String cadenaValidar = numRuc;
    Boolean certificadoCorrecto = Boolean.valueOf(false);
    Certificate cf = null;
    
    try {
      fis = new FileInputStream(input);
    } catch (FileNotFoundException e1) {
      throw new IllegalArgumentException("No se pudo encontrar el archivo: " + input, e1);
    } 
    
    try {
      KeyStore ks = KeyStore.getInstance("PKCS12");
      
      ks.load(fis, passPrivateKey.toCharArray());
      
      for (Enumeration<String> e = ks.aliases(); e.hasMoreElements() && 
        !certificadoCorrecto.booleanValue(); ) {
        alias = e.nextElement();
        log.debug("GenerarDocumentosServiceImpl.validaCertificado...Alias: " + alias);
        if (ks.isKeyEntry(alias)) {
          cf = ks.getCertificate(alias);
          log.debug("GenerarDocumentosServiceImpl.validaCertificado...cf.toString: " + cf.toString());
          cadena = cf.toString();
          
          if (cadena.indexOf(cadenaValidar) > 0) {
            certificadoCorrecto = Boolean.valueOf(true);
          }
        }
      
      }
    
    } catch (KeyStoreException e1) {
      try {
        fis.close();
      } catch (IOException iOException) {}
      
      throw new IllegalArgumentException("No se pudo procesar el certificado: " + input, e1);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalArgumentException("No se pudo procesar el certificado: " + input, e);
    } catch (CertificateException e) {
      throw new IllegalArgumentException("No se pudo procesar el certificado: " + input, e);
    } catch (IOException e) {
      throw new IllegalArgumentException("No se pudo procesar el certificado: " + input, e);
    } 


    
    if (!certificadoCorrecto.booleanValue()) {
      validacion = "El propietario del certificado no es el RUC " + numRuc;
    } else {
      validacion = "[ALIAS]:" + alias;
    } 
    log.debug("GenerarDocumentosServiceImpl.validaCertificado...Finalizando validacion de certificado");
    return validacion;
  }
}

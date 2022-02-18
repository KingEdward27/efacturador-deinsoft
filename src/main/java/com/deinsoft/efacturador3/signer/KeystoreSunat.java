package com.deinsoft.efacturador3.signer;

import com.deinsoft.efacturador3.signer.service.util.config.SimpleConfig;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class KeystoreSunat
{
  private KeyStore sunatKeyStore;
  private SimpleConfig certificateConfig;
  /*    */   
  public KeystoreSunat() throws KeyStoreException {
    try {
      this.certificateConfig = SimpleConfig.getInstance();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    this.sunatKeyStore = KeyStore.getInstance(this.certificateConfig.getKeystoreType());
    try {
      InputStream url = KeystoreSunat.class.getClassLoader().getResourceAsStream(this.certificateConfig.getKeystoreFile());
      
      if (url == null) {
        throw new KeyStoreException("No se encontro el almacen: " + this.certificateConfig.getKeystoreFile());
      }
      
      this.sunatKeyStore.load(url, this.certificateConfig.getKeystorePassword().toCharArray());
    } catch (NoSuchAlgorithmException e) {
      throw new KeyStoreException(e);
    } catch (CertificateException e) {
      throw new KeyStoreException(e);
    } catch (FileNotFoundException e) {
      throw new KeyStoreException(e);
    } catch (IOException e) {
      throw new KeyStoreException(e);
    } 
  }


  
  public PrivateKey getPrivateKey() throws KeyStoreException {
    PrivateKey privateKey;
    try {
      privateKey = (PrivateKey)this.sunatKeyStore.getKey(this.certificateConfig.getPrivatekeyAlias(), this.certificateConfig.getPrivatekeyPassword().toCharArray());
      if (privateKey == null) {
        throw new KeyStoreException("No se encontro el certificado: " + this.certificateConfig.getPrivatekeyAlias());
      }
    } catch (UnrecoverableKeyException e) {
      throw new KeyStoreException(e);
    } catch (NoSuchAlgorithmException e) {
      throw new KeyStoreException(e);
    } 
    return privateKey;
  }
  
  public X509Certificate getCertificate() throws KeyStoreException {
    return (X509Certificate)this.sunatKeyStore.getCertificate(this.certificateConfig.getPrivatekeyAlias());
  }
  
  public Certificate[] getCertificateChain() throws KeyStoreException {
    return this.sunatKeyStore.getCertificateChain(this.certificateConfig.getPrivatekeyAlias());
  }
  
  public X509Certificate getPublic() throws KeyStoreException {
    return getCertificate();
  }
}

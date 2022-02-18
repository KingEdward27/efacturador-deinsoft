package com.deinsoft.efacturador3.signer.service.util.config;


















public class SimpleConfig
  extends Config
{
  public static final String RESOURCE_NAME = "certificate.properties";
  private static SimpleConfig INSTANCE = new SimpleConfig();


  
  private SimpleConfig() {
    super("certificate.properties");
  }

  
  public static SimpleConfig getInstance() {
    return INSTANCE;
  }
  
  private SimpleConfig(String resourceName) {
    super(resourceName);
  }
  
  public String getKeystoreType() {
    return getString("keystore.type");
  }
  
  public String getKeystoreFile() {
    return getString("keystore.file");
  }
  
  public String getKeystorePassword() {
    return getString("keystore.password");
  }

  
  public String getPrivatekeyPassword() {
    return getString("privatekey.password");
  }
  
  public String getPrivatekeyAlias() {
    return getString("privatekey.alias");
  }
  
  public String getWebServiceUrl() {
    return getString("webservice.url");
  }
  
  public String getInitialPath() {
    return getString("initial.path");
  }
  public String getInitialFilename() {
    return getString("initial.filename");
  }
  
  public String getUserRucNumber() {
    return getString("user.ruc.number");
  }
  
  public String getUserName() {
    return getString("user.name");
  }
  
  public String getUserPassword() {
    return getString("user.password");
  }
  
  public String getUserCompleteName() {
    return getString("user.complete.name");
  }
}
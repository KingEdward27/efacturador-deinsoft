package com.deinsoft.efacturador3;

import com.deinsoft.efacturador3.config.XsltCpePath;






public class ConfigurationHolder
{
  private XsltCpePath xsltCpePath;
  private static final ConfigurationHolder instance = new ConfigurationHolder();
  /*    */   
  public static ConfigurationHolder getInstance() {
    return instance;
  }

  
  public void set(XsltCpePath xsltCpePath) {
    this.xsltCpePath = xsltCpePath;
  }

  
  public XsltCpePath getXsltCpePath() {
    return this.xsltCpePath;
  }
}
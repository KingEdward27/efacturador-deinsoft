/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.config;

/**
 *
 * @author EDWARD-PC
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class FacturadorConfiguration
  extends Configuration
{
  @Valid
  @NotNull
  private XsltCpePath xsltCpePath;
  
  @JsonProperty("xsltCpePath")
  public XsltCpePath getXsltCpePath() {
    return this.xsltCpePath;
  }
  
  @JsonProperty("xsltCpePath")
  public void setCpePath(XsltCpePath xsltCpePath) {
    this.xsltCpePath = xsltCpePath;
  }
}

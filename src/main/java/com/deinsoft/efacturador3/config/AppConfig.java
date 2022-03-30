package com.deinsoft.efacturador3.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AppConfig
{
  @NotEmpty
  @Value("${app.rootPath}")
  private String rootPath;
  
  @NotEmpty
  @Value("${plazoBoleta}")
  private String plazoBoleta;
  
  @NotEmpty
  @Value("${app.environment}")
  private String environment;
  
  @NotEmpty
  @Value("${app.sendEmail.email}")
  private String sendEmailEmail;

  @NotEmpty
  @Value("${app.sendEmail.password}")
  private String sendEmailPassword;
  
  @JsonProperty
  public String getRootPath() {
    return this.rootPath;
  }
  
  @JsonProperty
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

    public String getPlazoBoleta() {
        return plazoBoleta;
    }

    public void setPlazoBoleta(String plazoBoleta) {
        this.plazoBoleta = plazoBoleta;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getSendEmailEmail() {
        return sendEmailEmail;
    }

    public void setSendEmailEmail(String sendEmailEmail) {
        this.sendEmailEmail = sendEmailEmail;
    }

    public String getSendEmailPassword() {
        return sendEmailPassword;
    }

    public void setSendEmailPassword(String sendEmailPassword) {
        this.sendEmailPassword = sendEmailPassword;
    }
  
  
}

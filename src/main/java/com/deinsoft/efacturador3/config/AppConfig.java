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
  
  @JsonProperty
  public String getRootPath() {
    return this.rootPath;
  }
  
  @JsonProperty
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }
  
  
}

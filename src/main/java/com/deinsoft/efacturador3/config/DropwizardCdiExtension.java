/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.config;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




class DropwizardCdiExtension
  implements Extension
{
  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private final Set<String> names = Sets.newHashSet();
  
  void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
    this.logger.info("============================> beginning the scanning process");
  }
  
  <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
    String name = pat.getAnnotatedType().getJavaClass().getName();
    this.logger.debug("============================> scanning type: {}", name);
    this.names.add(name);
  }
  
  void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
    this.logger.info("============================> finished the scanning process");
  }
  
  public Set<String> getNames() {
    return this.names;
  }
}


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\sistema\facturador\config\DropwizardCdiExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */

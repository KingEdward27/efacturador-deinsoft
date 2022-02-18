/*    */ package com.deinsoft.efacturador3.config;
/*    */ 
/*    */ import io.dropwizard.Bundle;
/*    */ import io.dropwizard.setup.Bootstrap;
/*    */ import io.dropwizard.setup.Environment;
/*    */ import java.util.EventListener;
/*    */ import org.jboss.weld.environment.servlet.BeanManagerResourceBindingListener;
/*    */ import org.jboss.weld.environment.servlet.Listener;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WeldBundle
/*    */   implements Bundle
/*    */ {
/*    */   public void initialize(Bootstrap<?> bootstrap) {}
/*    */   
/*    */   public void run(Environment environment) {
/* 18 */     environment.getApplicationContext().addEventListener((EventListener)new BeanManagerResourceBindingListener());
/* 19 */     environment.getApplicationContext().addEventListener((EventListener)new Listener());
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\sistema\facturador\config\WeldBundle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
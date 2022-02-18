/*     */ package com.deinsoft.efacturador3.wssunat.service;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Service;
/*     */ import javax.xml.ws.WebEndpoint;
/*     */ import javax.xml.ws.WebServiceClient;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @WebServiceClient(name = "billService", wsdlLocation = "https://www.sunat.gob.pe/ol-ti-itcpfegem/billService?wsdl", targetNamespace = "http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/")
/*     */ public class BillService_Service
/*     */   extends Service
/*     */ {
/*     */   public static final URL WSDL_LOCATION;
/*  24 */   public static final QName SERVICE = new QName("http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", "billService");
/*  25 */   public static final QName BillServicePort = new QName("http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", "BillServicePort");
/*  26 */   public static final QName BillServicePort0 = new QName("http://service.gem.factura.comppago.registro.servicio.sunat.gob.pe/", "BillServicePort.0");
/*     */   static {
/*  28 */     URL url = null;
/*     */     try {
/*  30 */       url = new URL("https://www.sunat.gob.pe/ol-ti-itcpfegem/billService?wsdl");
/*  31 */     } catch (MalformedURLException e) {
/*  32 */       Logger.getLogger(BillService_Service.class.getName())
/*  33 */         .log(Level.INFO, "Can not initialize the default wsdl from {0}", "https://www.sunat.gob.pe/ol-ti-itcpfegem/billService?wsdl");
/*     */     } 
/*     */     
/*  36 */     WSDL_LOCATION = url;
/*     */   }
/*     */   
/*     */   public BillService_Service(URL wsdlLocation) {
/*  40 */     super(wsdlLocation, SERVICE);
/*     */   }
/*     */   /*     */   
/*     */   public BillService_Service(URL wsdlLocation, QName serviceName) {
/*  44 */     super(wsdlLocation, serviceName);
/*     */   }
/*     */   /*     */   
/*     */   public BillService_Service() {
/*  48 */     super(WSDL_LOCATION, SERVICE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BillService_Service(WebServiceFeature... features) {
/*  55 */     super(WSDL_LOCATION, SERVICE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BillService_Service(URL wsdlLocation, WebServiceFeature... features) {
/*  62 */     super(wsdlLocation, SERVICE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   /*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BillService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
/*  69 */     super(wsdlLocation, serviceName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @WebEndpoint(name = "BillServicePort")
/*     */   public BillService getBillServicePort() {
/*  79 */     return getPort(BillServicePort, BillService.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @WebEndpoint(name = "BillServicePort")
/*     */   public BillService getBillServicePort(WebServiceFeature... features) {
/*  91 */     return getPort(BillServicePort, BillService.class, features);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @WebEndpoint(name = "BillServicePort.0")
/*     */   public BillService getBillServicePort0() {
/* 100 */     return getPort(BillServicePort0, BillService.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @WebEndpoint(name = "BillServicePort.0")
/*     */   public BillService getBillServicePort0(WebServiceFeature... features) {
/* 112 */     return getPort(BillServicePort0, BillService.class, features);
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\service\BillService_Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
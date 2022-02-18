/*    */ package com.deinsoft.efacturador3.wssunat.ws.client;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.Service;
/*    */ import javax.xml.ws.WebEndpoint;
/*    */ import javax.xml.ws.WebServiceClient;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ import javax.xml.ws.WebServiceFeature;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @WebServiceClient(name = "billConsultService", targetNamespace = "http://service.ws.consulta.comppago.electronico.registro.servicio2.sunat.gob.pe/", wsdlLocation = "https://www.sunat.gob.pe/ol-it-wsconscpegem/billConsultService?wsdl")
/*    */ public class BillConsultService
/*    */   extends Service
/*    */ {
/*    */   private static final URL BILLCONSULTSERVICE_WSDL_LOCATION;
/*    */   private static final WebServiceException BILLCONSULTSERVICE_EXCEPTION;
/* 27 */   private static final QName BILLCONSULTSERVICE_QNAME = new QName("http://service.ws.consulta.comppago.electronico.registro.servicio2.sunat.gob.pe/", "billConsultService");
/*    */   
/*    */   static {
/* 30 */     URL url = null;
/* 31 */     WebServiceException e = null;
/*    */     try {
/* 33 */       url = new URL("https://www.sunat.gob.pe/ol-it-wsconscpegem/billConsultService?wsdl");
/* 34 */     } catch (MalformedURLException ex) {
/* 35 */       e = new WebServiceException(ex);
/*    */     } 
/* 37 */     BILLCONSULTSERVICE_WSDL_LOCATION = url;
/* 38 */     BILLCONSULTSERVICE_EXCEPTION = e;
/*    */   }
/*    */   /*    */   
/*    */   public BillConsultService(WebServiceFeature... features) {
/* 42 */     super(__getWsdlLocation(), BILLCONSULTSERVICE_QNAME, features);
/*    */   }
/*    */   /*    */   
/*    */   public BillConsultService(URL wsdlLocation) {
/* 46 */     super(wsdlLocation, BILLCONSULTSERVICE_QNAME);
/*    */   }
/*    */   /*    */   
/*    */   public BillConsultService(URL wsdlLocation, WebServiceFeature... features) {
/* 50 */     super(wsdlLocation, BILLCONSULTSERVICE_QNAME, features);
/*    */   }
/*    */   /*    */   
/*    */   public BillConsultService(URL wsdlLocation, QName serviceName) {
/* 54 */     super(wsdlLocation, serviceName);
/*    */   }
/*    */   /*    */   
/*    */   public BillConsultService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
/* 58 */     super(wsdlLocation, serviceName, features);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @WebEndpoint(name = "BillConsultServicePort")
/*    */   public BillService getBillConsultServicePort() {
/* 68 */     return getPort(new QName("http://service.ws.consulta.comppago.electronico.registro.servicio2.sunat.gob.pe/", "BillConsultServicePort"), BillService.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @WebEndpoint(name = "BillConsultServicePort")
/*    */   public BillService getBillConsultServicePort(WebServiceFeature... features) {
/* 80 */     return getPort(new QName("http://service.ws.consulta.comppago.electronico.registro.servicio2.sunat.gob.pe/", "BillConsultServicePort"), BillService.class, features);
/*    */   }
/*    */   
/*    */   private static URL __getWsdlLocation() {
/* 84 */     if (BILLCONSULTSERVICE_EXCEPTION != null) {
/* 85 */       throw BILLCONSULTSERVICE_EXCEPTION;
/*    */     }
/* 87 */     return BILLCONSULTSERVICE_WSDL_LOCATION;
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\ws\client\BillConsultService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.deinsoft.efacturador3.wssunat.ws.client;
/*    */ 
/*    */ import java.util.Set;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.soap.SOAPElement;
/*    */ import javax.xml.soap.SOAPEnvelope;
/*    */ import javax.xml.soap.SOAPHeader;
/*    */ import javax.xml.ws.handler.MessageContext;
/*    */ import javax.xml.ws.handler.soap.SOAPHandler;
/*    */ import javax.xml.ws.handler.soap.SOAPMessageContext;
/*    */ 
/*    */ 
/*    */ public class WSSecurityHeaderSOAPHandler
/*    */   implements SOAPHandler<SOAPMessageContext>
/*    */ {
/*    */   private static final String SOAP_ELEMENT_PASSWORD = "Password";
/*    */   private static final String SOAP_ELEMENT_USERNAME = "Username";
/*    */   private static final String SOAP_ELEMENT_USERNAME_TOKEN = "UsernameToken";
/*    */   private static final String SOAP_ELEMENT_SECURITY = "Security";
/*    */   private static final String NAMESPACE_SECURITY = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
/*    */   private static final String PREFIX_SECURITY = "wsse";
/*    */   private String usernameText;
/*    */   private String passwordText;
/*    */   /*    */   
/*    */   public WSSecurityHeaderSOAPHandler(String usernameText, String passwordText) {
/* 26 */     this.usernameText = usernameText;
/* 27 */     this.passwordText = passwordText;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean handleMessage(SOAPMessageContext soapMessageContext) {
/* 32 */     Boolean outboundProperty = (Boolean)soapMessageContext.get("javax.xml.ws.handler.message.outbound");
/*    */     
/* 34 */     if (outboundProperty.booleanValue()) {
/*    */       
/*    */       try {
/* 37 */         SOAPEnvelope soapEnvelope = soapMessageContext.getMessage().getSOAPPart().getEnvelope();
/*    */         
/* 39 */         SOAPHeader header = soapEnvelope.getHeader();
/* 40 */         if (header == null) {
/* 41 */           header = soapEnvelope.addHeader();
/*    */         }
/*    */         
/* 44 */         SOAPElement soapElementSecurityHeader = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
/*    */ 
/*    */         
/* 47 */         SOAPElement soapElementUsernameToken = soapElementSecurityHeader.addChildElement("UsernameToken", "wsse");
/* 48 */         SOAPElement soapElementUsername = soapElementUsernameToken.addChildElement("Username", "wsse");
/* 49 */         soapElementUsername.addTextNode(this.usernameText);
/*    */         
/* 51 */         SOAPElement soapElementPassword = soapElementUsernameToken.addChildElement("Password", "wsse");
/* 52 */         soapElementPassword.addTextNode(this.passwordText);
/*    */       }
/* 54 */       catch (Exception e) {
/* 55 */         throw new RuntimeException("Error on wsSecurityHandler: " + e.getMessage());
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close(MessageContext context) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean handleFault(SOAPMessageContext context) {
/* 71 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<QName> getHeaders() {
/* 77 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\ws\client\WSSecurityHeaderSOAPHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
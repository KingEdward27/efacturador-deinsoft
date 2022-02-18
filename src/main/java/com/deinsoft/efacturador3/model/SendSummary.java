/*    */ package com.deinsoft.efacturador3.model;
/*    */ 
/*    */ import javax.activation.DataHandler;
/*    */ import javax.xml.bind.annotation.XmlAccessType;
/*    */ import javax.xml.bind.annotation.XmlAccessorType;
/*    */ import javax.xml.bind.annotation.XmlMimeType;
/*    */ import javax.xml.bind.annotation.XmlType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @XmlAccessorType(XmlAccessType.FIELD)
/*    */ @XmlType(name = "sendSummary", propOrder = {"fileName", "contentFile"})
/*    */ public class SendSummary
/*    */ {
/*    */   protected String fileName;
/*    */   @XmlMimeType("application/octet-stream")
/*    */   protected DataHandler contentFile;
/*    */   
/*    */   public String getFileName() {
/* 51 */     return this.fileName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFileName(String value) {
/* 63 */     this.fileName = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DataHandler getContentFile() {
/* 75 */     return this.contentFile;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setContentFile(DataHandler value) {
/* 87 */     this.contentFile = value;
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\model\SendSummary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
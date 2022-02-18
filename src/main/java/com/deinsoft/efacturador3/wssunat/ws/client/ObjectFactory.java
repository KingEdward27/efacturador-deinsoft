/*     */ package com.deinsoft.efacturador3.wssunat.ws.client;
/*     */ 
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.XmlElementDecl;
/*     */ import javax.xml.bind.annotation.XmlRegistry;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlRegistry
/*     */ public class ObjectFactory
/*     */ {
/*  27 */   private static final QName _GetStatusCdr_QNAME = new QName("http://service.sunat.gob.pe", "getStatusCdrConsulta");
/*  28 */   private static final QName _GetStatusResponse_QNAME = new QName("http://service.sunat.gob.pe", "getStatusResponseConsulta");
/*  29 */   private static final QName _GetStatus_QNAME = new QName("http://service.sunat.gob.pe", "getStatusConsulta");
/*  30 */   private static final QName _GetStatusCdrResponse_QNAME = new QName("http://service.sunat.gob.pe", "getStatusCdrResponseConsulta");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GetStatusResponse createGetStatusResponse() {
/*  44 */     return new GetStatusResponse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GetStatusCdr createGetStatusCdr() {
/*  52 */     return new GetStatusCdr();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GetStatusCdrResponse createGetStatusCdrResponse() {
/*  60 */     return new GetStatusCdrResponse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GetStatus createGetStatus() {
/*  68 */     return new GetStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusResponse createStatusResponse() {
/*  76 */     return new StatusResponse();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "getStatusCdrConsulta")
/*     */   public JAXBElement<GetStatusCdr> createGetStatusCdr(GetStatusCdr value) {
/*  85 */     return new JAXBElement<>(_GetStatusCdr_QNAME, GetStatusCdr.class, null, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "getStatusResponseConsulta")
/*     */   public JAXBElement<GetStatusResponse> createGetStatusResponse(GetStatusResponse value) {
/*  94 */     return new JAXBElement<>(_GetStatusResponse_QNAME, GetStatusResponse.class, null, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "getStatusConsulta")
/*     */   public JAXBElement<GetStatus> createGetStatus(GetStatus value) {
/* 103 */     return new JAXBElement<>(_GetStatus_QNAME, GetStatus.class, null, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "getStatusCdrResponseConsulta")
/*     */   public JAXBElement<GetStatusCdrResponse> createGetStatusCdrResponse(GetStatusCdrResponse value) {
/* 112 */     return new JAXBElement<>(_GetStatusCdrResponse_QNAME, GetStatusCdrResponse.class, null, value);
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\ws\client\ObjectFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
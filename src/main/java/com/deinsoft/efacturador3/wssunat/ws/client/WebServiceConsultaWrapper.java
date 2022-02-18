/*     */ package com.deinsoft.efacturador3.wssunat.ws.client;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.ws.BindingProvider;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import javax.xml.xpath.XPath;
/*     */ import javax.xml.xpath.XPathConstants;
/*     */ import javax.xml.xpath.XPathFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebServiceConsultaWrapper
/*     */ {
/*  31 */   private URL url = null;
/*     */   /*     */   
/*     */   public WebServiceConsultaWrapper(String urlWebServiceCdr) {
/*     */     try {
/*  35 */       this.url = new URL(urlWebServiceCdr);
/*  36 */     } catch (MalformedURLException e) {
/*  37 */       throw new RuntimeException("La url esta mal formada", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer obtenerEstadoCdr(String usuario, String password, String nombreArchivo, String rucComprobante, String tipoComprobante, String serieComprobante, Integer numeroComprobante, String confirmaExito) {
/*  43 */     Integer resultado = Integer.valueOf(-1);
/*     */ 
/*     */     
/*     */     try {
/*  47 */       BillConsultService client = new BillConsultService(this.url);
/*  48 */       BillService service = client.getBillConsultServicePort();
/*     */       
/*  50 */       byte[] contenido = null;
/*     */ 
/*     */       
/*  53 */       int connectionTimeOutInMs = 5000;
/*  54 */       Map<String, Object> context = ((BindingProvider)service).getRequestContext();
/*  55 */       context.put("com.sun.xml.internal.ws.connect.timeout", Integer.valueOf(connectionTimeOutInMs));
/*  56 */       context.put("com.sun.xml.internal.ws.request.timeout", Integer.valueOf(connectionTimeOutInMs));
/*  57 */       context.put("com.sun.xml.ws.request.timeout", Integer.valueOf(connectionTimeOutInMs));
/*  58 */       context.put("com.sun.xml.ws.connect.timeout", Integer.valueOf(connectionTimeOutInMs));
/*     */ 
/*     */ 
/*     */       
/*  62 */       BindingProvider bindingProvider = (BindingProvider)service;
/*     */       
/*  64 */       List<Handler> handlerChain = new ArrayList<>();
/*  65 */       handlerChain.add(new WSSecurityHeaderSOAPHandler(usuario, password));
/*  66 */       bindingProvider.getBinding().setHandlerChain(handlerChain);
/*     */ 
/*     */       
/*  69 */       StatusResponse response = service.getStatusCdr(rucComprobante, tipoComprobante, serieComprobante, numeroComprobante);
/*  70 */       contenido = response.getContent();
/*     */ 
/*     */       
/*  73 */       if (contenido == null) {
/*  74 */         throw new Exception("No se encontró archivo en SUNAT.");
/*     */       }
/*  76 */       byte[] data = unzip(contenido);
/*     */       
/*  78 */       Document doc = byteToDocument(data);
/*  79 */       XPath xPath = XPathFactory.newInstance().newXPath();
/*     */ 
/*     */       
/*  82 */       NodeList warningsNode = (NodeList)xPath.evaluate("/*[local-name()='ApplicationResponse']/*[local-name()='Note']", doc
/*  83 */           .getDocumentElement(), XPathConstants.NODESET);
/*  84 */       List<String> lstWarnings = new ArrayList<>();
/*  85 */       for (int i = 0; i < warningsNode.getLength(); i++) {
/*  86 */         Node show = warningsNode.item(i);
/*  87 */         lstWarnings.add(show.getTextContent());
/*     */       } 
/*     */       
/*  90 */       String codigo = response.getStatusCode();
/*     */ 
/*     */       
/*  93 */       if (confirmaExito.equals(codigo))
/*  94 */       { if (lstWarnings.size() > 0) {
/*  95 */           resultado = Integer.valueOf(1);
/*     */         } else {
/*  97 */           resultado = Integer.valueOf(0);
/*     */         }  }
/*  99 */       else { resultado = new Integer(codigo); }
/*     */ 
/*     */       
/* 102 */       FileOutputStream fos = new FileOutputStream(nombreArchivo);
/* 103 */       fos.write(contenido);
/* 104 */       fos.close();
/*     */     }
/* 106 */     catch (Exception e) {
/* 107 */       e.printStackTrace();
/* 108 */       throw new RuntimeException("Error al verificar estado del ticket", e);
/*     */     } 
/*     */     
/* 111 */     return resultado;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] unzip(byte[] data) throws Exception {
/*     */     try {
/* 117 */       ByteArrayInputStream bis = new ByteArrayInputStream(data);
/* 118 */       ZipInputStream srcIs = new ZipInputStream(bis);
/*     */       
/* 120 */       ByteArrayOutputStream destOs = new ByteArrayOutputStream();
/* 121 */       ZipEntry entry = null;
/* 122 */       while (null != (entry = srcIs.getNextEntry())) {
/* 123 */         if (entry.getName().endsWith(".xml")) {
/*     */           
/* 125 */           int count = 0;
/* 126 */           byte[] buffer = new byte[2048];
/* 127 */           while ((count = srcIs.read(buffer)) > 0) {
/* 128 */             destOs.write(buffer, 0, count);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 133 */       destOs.flush();
/* 134 */       byte[] b = destOs.toByteArray();
/* 135 */       destOs.close();
/*     */       
/* 137 */       destOs.close();
/* 138 */       srcIs.close();
/* 139 */       bis.close();
/* 140 */       return b;
/* 141 */     } catch (Exception e) {
/* 142 */       throw new Exception("Error al descomprimir la constancia", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Document byteToDocument(byte[] data) throws Exception {
/* 153 */     InputStream ipEntrada = new ByteArrayInputStream(data);
/* 154 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/* 155 */     dbf.setNamespaceAware(true);
/*     */     try {
/* 157 */       Document doc = dbf.newDocumentBuilder().parse(ipEntrada);
/* 158 */       return doc;
/* 159 */     } catch (SAXException e) {
/* 160 */       throw new Exception("Error al leer la constancia", e);
/* 161 */     } catch (IOException e) {
/* 162 */       throw new Exception("Error al leer la constancia", e);
/* 163 */     } catch (ParserConfigurationException e) {
/* 164 */       throw new Exception("Error al leer la constancia", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\ws\client\WebServiceConsultaWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
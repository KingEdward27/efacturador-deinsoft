/*    */ package com.deinsoft.efacturador3.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import javax.xml.transform.Result;
/*    */ import javax.xml.transform.Source;
/*    */ import javax.xml.transform.Templates;
/*    */ import javax.xml.transform.Transformer;
/*    */ import javax.xml.transform.TransformerException;
/*    */ import javax.xml.transform.TransformerFactory;
/*    */ import javax.xml.transform.stream.StreamResult;
/*    */ import javax.xml.transform.stream.StreamSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XslTransformer
/*    */ {
/* 21 */   private TransformerFactory factory = TransformerFactory.newInstance();
/*    */ 
/*    */   
/*    */   public void process(Reader xmlFile, Reader xslFile, Writer output) throws TransformerException {
/* 25 */     process(new StreamSource(xmlFile), new StreamSource(xslFile), new StreamResult(output));
/*    */   }
/*    */   
/*    */   public void process(File xmlFile, File xslFile, Writer output) throws TransformerException {
/* 29 */     process(new StreamSource(xmlFile), new StreamSource(xslFile), new StreamResult(output));
/*    */   }
/*    */   
/*    */   public void process(File xmlFile, File xslFile, OutputStream out) throws TransformerException {
/* 33 */     process(new StreamSource(xmlFile), new StreamSource(xslFile), new StreamResult(out));
/*    */   }
/*    */   
/*    */   public void process(Source xml, Source xsl, Result result) throws TransformerException {
/* 37 */     Templates template = this.factory.newTemplates(xsl);
/* 38 */     Transformer transformer = template.newTransformer();
/* 39 */     transformer.transform(xml, result);
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\sistema\facturado\\util\XslTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
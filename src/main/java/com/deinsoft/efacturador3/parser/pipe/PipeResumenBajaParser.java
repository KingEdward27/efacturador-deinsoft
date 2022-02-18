/*     */ package com.deinsoft.efacturador3.parser.pipe;
/*     */ 
/*     */ import com.deinsoft.efacturador3.model.Contribuyente;
/*     */ import com.deinsoft.efacturador3.parser.Parser;
/*     */ import com.deinsoft.efacturador3.parser.ParserException;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PipeResumenBajaParser
/*     */   extends PipeCpeAbstractParser
/*     */   implements Parser
/*     */ {
/*  27 */   private static final Logger log = LoggerFactory.getLogger(PipeResumenBajaParser.class);
/*     */ 
/*     */   
/*  30 */   private static String plantillaSeleccionada = "ConvertirRBajasXML.ftl";
/*     */ 
/*     */   
/*     */   private String archivoCabecera;
/*     */ 
/*     */   
/*     */   private String nombreArchivo;
/*     */   
/*     */   private Contribuyente contri;
/*     */ 
/*     */   /*     */ 
/*     */   
/*     */   public PipeResumenBajaParser(Contribuyente contri, String[] archivos, String nombreArchivo) {
/*  42 */     this.contri = contri;
/*     */     
/*  44 */     this.nombreArchivo = nombreArchivo;
/*  45 */     this.archivoCabecera = archivos[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> pipeToMap() throws ParserException {
/*  50 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Inicio Procesamiento");
/*     */     
/*  52 */     Integer error = new Integer(0);
/*     */ 
/*     */     
/*  55 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Leyendo Archivo: " + this.archivoCabecera);
/*  56 */     Map<String, Object> resumenGeneral = new HashMap<>();
/*  57 */     Map<String, Object> resumenBajas = null;
/*  58 */     List<Map<String, Object>> listaResumenBajas = new ArrayList<>();
/*     */     
/*  60 */     Path file = Paths.get(this.archivoCabecera, new String[0]);
/*  61 */     try(InputStream in = Files.newInputStream(file, new java.nio.file.OpenOption[0]); 
/*  62 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/*  63 */       String cadena = null;
/*     */       
/*  65 */       Integer linea = Integer.valueOf(0);
/*     */       
/*  67 */       while ((cadena = reader.readLine()) != null) {
/*     */         
/*  69 */         String[] registro = cadena.split("\\|");
/*     */         
/*  71 */         if (registro.length != 5 && error.intValue() == 0) {
/*  72 */           error = new Integer(1);
/*  73 */           throw new IllegalArgumentException("El archivo CBA no contiene la cantidad de columnas esperada (5 columnas).");
/*     */         } 
/*     */ 
/*     */         
/*  77 */         Integer integer1 = linea, integer2 = linea = Integer.valueOf(linea.intValue() + 1);
/*     */ 
/*     */         
/*  80 */         log.debug("SoftwareFacturadorController.formatoResumenBajas...nombreArchivo: " + this.nombreArchivo);
/*  81 */         String[] idArchivo = this.nombreArchivo.split("\\-");
/*  82 */         String idComunicacion = idArchivo[1] + "-" + idArchivo[2] + "-" + idArchivo[3];
/*     */ 
/*     */         
/*  85 */         String fechaDocumentoBaja = registro[0];
/*  86 */         String fechaComunicacionBaja = registro[1];
/*  87 */         String tipoDocumento = registro[2];
/*  88 */         String serieNumeroDocumento = registro[3];
/*  89 */         String motivoBajaDocumento = registro[4];
/*  90 */         String[] nroDocumento = serieNumeroDocumento.split("\\-");
/*  91 */         String identificadorFirmaSwf = "SIGN";
/*  92 */         Random calcularRnd = new Random();
/*  93 */         Integer codigoFacturadorSwf = Integer.valueOf((int)(calcularRnd.nextDouble() * 1000000.0D));
/*     */         
/*  95 */         if (linea.intValue() == 1) {
/*  96 */           resumenGeneral.put("nombreComercialSwf", this.contri.getNombreComercial());
/*  97 */           resumenGeneral.put("razonSocialSwf", this.contri.getRazonSocial());
/*  98 */           resumenGeneral.put("nroRucEmisorSwf", this.contri.getNumRuc());
/*  99 */           resumenGeneral.put("tipDocuEmisorSwf", "6");
/* 100 */           resumenGeneral.put("fechaDocumentoBaja", fechaDocumentoBaja);
/* 101 */           resumenGeneral.put("fechaComunicacioBaja", fechaComunicacionBaja);
/*     */           
/* 103 */           resumenGeneral.put("ublVersionIdSwf", "2.0");
/* 104 */           resumenGeneral.put("idComunicacion", idComunicacion);
/* 105 */           resumenGeneral.put("CustomizationIdSwf", "1.0");
/* 106 */           resumenGeneral.put("identificadorFacturadorSwf", "Elaborado por Sistema de Emision Electronica Facturador SUNAT (SEE-SFS) 1.3.2");
/*     */           
/* 108 */           resumenGeneral.put("codigoFacturadorSwf", codigoFacturadorSwf.toString());
/* 109 */           resumenGeneral.put("identificadorFirmaSwf", identificadorFirmaSwf);
/*     */           
/* 111 */           resumenBajas = new HashMap<>();
/* 112 */           resumenBajas.put("tipoDocumentoBaja", tipoDocumento);
/* 113 */           resumenBajas.put("serieDocumentoBaja", nroDocumento[0]);
/* 114 */           resumenBajas.put("nroDocumentoBaja", nroDocumento[1]);
/* 115 */           resumenBajas.put("motivoBajaDocumento", motivoBajaDocumento);
/* 116 */           resumenBajas.put("linea", linea);
/*     */         }
/*     */         else {
/*     */           
/* 120 */           resumenBajas = new HashMap<>();
/* 121 */           resumenBajas.put("tipoDocumentoBaja", tipoDocumento);
/* 122 */           resumenBajas.put("serieDocumentoBaja", nroDocumento[0]);
/* 123 */           resumenBajas.put("nroDocumentoBaja", nroDocumento[1]);
/* 124 */           resumenBajas.put("motivoBajaDocumento", motivoBajaDocumento);
/* 125 */           resumenBajas.put("linea", linea);
/*     */         } 
/*     */         
/* 128 */         listaResumenBajas.add(resumenBajas);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 134 */       resumenGeneral.put("listaResumen", listaResumenBajas);
/*     */       
/* 136 */       log.debug("SoftwareFacturadorController.formatoResumenBajas...Fin Procesamiento");
/*     */       
/* 138 */       return resumenGeneral;
/*     */     }
/* 140 */     catch (IOException x) {
/* 141 */       throw new ParserException("No se pudo leer el archivo cabecera: " + this.archivoCabecera, x);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] parse(String templatesPath) throws ParserException {
/* 148 */     return parse(templatesPath, plantillaSeleccionada);
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\facturador\parser\pipe\PipeResumenBajaParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
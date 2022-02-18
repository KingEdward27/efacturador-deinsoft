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
/*     */ 
/*     */ 
/*     */ public class PipePercepcionParser
/*     */   extends PipeCpeAbstractParser
/*     */   implements Parser
/*     */ {
/*  29 */   private static final Logger log = LoggerFactory.getLogger(PipePercepcionParser.class);
/*     */ 
/*     */   
/*  32 */   private static String plantillaSeleccionada = "ConvertirPercepcionXML.ftl";
/*     */ 
/*     */   
/*     */   private String archivoCabecera;
/*     */ 
/*     */   
/*     */   private String archivoDetalle;
/*     */   
/*     */   private String nombreArchivo;
/*     */   
/*     */   private Contribuyente contri;
/*     */ 
/*     */   /*     */ 
/*     */   
/*     */   public PipePercepcionParser(Contribuyente contri, String[] archivos, String nombreArchivo) {
/*  46 */     this.contri = contri;
/*  47 */     this.nombreArchivo = nombreArchivo;
/*     */     
/*  49 */     this.archivoCabecera = archivos[0];
/*  50 */     this.archivoDetalle = archivos[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> pipeToMap() throws ParserException {
/*  55 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Inicio Procesamiento");
/*     */ 
/*     */     
/*  58 */     String[] datosArchivo = this.nombreArchivo.split("\\-");
/*     */     
/*  60 */     String identificadorFirmaSwf = "SIGN";
/*  61 */     Random calcularRnd = new Random();
/*  62 */     Integer codigoFacturadorSwf = Integer.valueOf((int)(calcularRnd.nextDouble() * 1000000.0D));
/*     */ 
/*     */     
/*  65 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Leyendo Archivo: " + this.archivoCabecera);
/*  66 */     Map<String, Object> Percepcion = new HashMap<>();
/*     */ 
/*     */     
/*  69 */     Path fileCabecera = Paths.get(this.archivoCabecera, new String[0]);
/*     */     
/*  71 */     if (!Files.exists(fileCabecera, new java.nio.file.LinkOption[0]))
/*     */     {
/*  73 */       throw new ParserException("El archivo no existe: " + this.archivoCabecera);
/*     */     }
/*     */ 
/*     */     
/*  77 */     try(InputStream in = Files.newInputStream(fileCabecera, new java.nio.file.OpenOption[0]); 
/*  78 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/*  79 */       String cadena = null;
/*     */       
/*  81 */       while ((cadena = reader.readLine()) != null) {
/*  82 */         String[] registro = cadena.split("\\|");
/*     */         
/*  84 */         if (registro.length != 19) {
/*  85 */           throw new ParserException("El archivo cabecera no continene la cantidad de datos esperada (19 columnas).");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*  90 */         Percepcion = new HashMap<>();
/*  91 */         Percepcion.put("fecEmision", registro[0]);
/*  92 */         Percepcion.put("nroDocIdeReceptor", registro[1]);
/*  93 */         Percepcion.put("tipDocIdeReceptor", registro[2]);
/*  94 */         Percepcion.put("desNomComReceptor", registro[3]);
/*     */         
/*  96 */         Percepcion.put("desUbiReceptor", registro[4]);
/*  97 */         Percepcion.put("desDirReceptor", registro[5]);
/*  98 */         Percepcion.put("desUrbReceptor", registro[6]);
/*  99 */         Percepcion.put("desDepReceptor", registro[7]);
/* 100 */         Percepcion.put("desProReceptor", registro[8]);
/* 101 */         Percepcion.put("desDisReceptor", registro[9]);
/* 102 */         Percepcion.put("codPaisReceptor", registro[10]);
/* 103 */         Percepcion.put("rznSocialReceptor", registro[11]);
/*     */         
/* 105 */         Percepcion.put("codRegPercepcion", registro[12]);
/* 106 */         Percepcion.put("tasPercepcion", registro[13]);
/* 107 */         Percepcion.put("desObsPercepcion", registro[14]);
/* 108 */         Percepcion.put("mtoTotPercepcion", registro[15]);
/* 109 */         Percepcion.put("tipMonPercepcion", registro[16]);
/* 110 */         Percepcion.put("mtoImpTotPagPercepcion", registro[17]);
/* 111 */         Percepcion.put("tipMonImpTotPagPercepcion", registro[18]);
/*     */ 
/*     */         
/* 114 */         Percepcion.put("ublVersionIdSwf", "2.0");
/* 115 */         Percepcion.put("CustomizationIdSwf", "1.0");
/* 116 */         Percepcion.put("nroCdpSwf", datosArchivo[2] + "-" + datosArchivo[3]);
/* 117 */         Percepcion.put("tipCdpSwf", datosArchivo[1]);
/*     */         
/* 119 */         Percepcion.put("nroRucEmisorSwf", this.contri.getNumRuc());
/* 120 */         Percepcion.put("tipDocuEmisorSwf", "6");
/* 121 */         Percepcion.put("nombreComercialSwf", this.contri.getNombreComercial());
/* 122 */         Percepcion.put("razonSocialSwf", this.contri.getRazonSocial());
/* 123 */         Percepcion.put("ubigeoDomFiscalSwf", this.contri.getDireccion().getUbigeo());
/* 124 */         Percepcion.put("direccionDomFiscalSwf", this.contri.getDireccion().getDireccion());
/* 125 */         Percepcion.put("deparSwf", this.contri.getDireccion().getDepar());
/* 126 */         Percepcion.put("provinSwf", this.contri.getDireccion().getProvin());
/* 127 */         Percepcion.put("distrSwf", this.contri.getDireccion().getDistr());
/* 128 */         Percepcion.put("urbanizaSwf", this.contri.getDireccion().getUrbaniza());
/*     */         
/* 130 */         Percepcion.put("paisDomFiscalSwf", "PE");
/* 131 */         Percepcion.put("tipoCodigoMonedaSwf", "01");
/*     */         
/* 133 */         Percepcion.put("identificadorFacturadorSwf", "Elaborado por Sistema de Emision Electronica Facturador SUNAT (SEE-SFS) 1.3.2");
/* 134 */         Percepcion.put("codigoFacturadorSwf", codigoFacturadorSwf.toString());
/* 135 */         Percepcion.put("identificadorFirmaSwf", identificadorFirmaSwf);
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 143 */     catch (IOException x) {
/* 144 */       throw new ParserException("No se pudo leer el archivo cabecera: " + this.archivoCabecera, x);
/*     */     } 
/*     */ 
/*     */     
/* 148 */     Path fileDetalle = Paths.get(this.archivoDetalle, new String[0]);
/*     */     
/* 150 */     if (!Files.exists(fileDetalle, new java.nio.file.LinkOption[0]))
/*     */     {
/* 152 */       throw new ParserException("El archivo no existe: " + this.archivoDetalle);
/*     */     }
/*     */ 
/*     */     
/* 156 */     List<Map<String, Object>> listaDetalle = new ArrayList<>();
/* 157 */     Map<String, Object> detalle = null;
/*     */     
/* 159 */     try(InputStream in = Files.newInputStream(fileDetalle, new java.nio.file.OpenOption[0]); 
/* 160 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/* 161 */       String cadena = null;
/*     */       
/* 163 */       Integer linea = Integer.valueOf(0);
/*     */       
/* 165 */       while ((cadena = reader.readLine()) != null) {
/* 166 */         String[] registro = cadena.split("\\|");
/* 167 */         if (registro.length != 18 && registro.length != 5)
/*     */         {
/* 169 */           throw new ParserException("El archivo detalle no continene la cantidad de datos esperada (18 columnas).");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 174 */         linea = Integer.valueOf(linea.intValue() + 1);
/* 175 */         detalle = new HashMap<>();
/* 176 */         detalle.put("tipDocRelacionado", registro[0]);
/* 177 */         detalle.put("nroDocRelacionado", registro[1]);
/* 178 */         detalle.put("fecEmiDocRelacionado", registro[2]);
/* 179 */         detalle.put("mtoImpTotDocRelacionado", registro[3]);
/* 180 */         detalle.put("tipMonDocRelacionado", registro[4]);
/* 181 */         detalle.put("fecPagDocRelacionado", registro[5]);
/* 182 */         detalle.put("nroPagDocRelacionado", registro[6]);
/* 183 */         detalle.put("mtoPagDocRelacionado", registro[7]);
/* 184 */         detalle.put("tipMonPagDocRelacionado", registro[8]);
/*     */         
/* 186 */         detalle.put("mtoPerDocRelacionado", registro[9]);
/* 187 */         detalle.put("tipMonPerDocRelacionado", registro[10]);
/* 188 */         detalle.put("fecPerDocRelacionado", registro[11]);
/* 189 */         detalle.put("mtoTotPagNetoDocRelacionado", registro[12]);
/* 190 */         detalle.put("tipMonTotPagNetoDocRelacionado", registro[13]);
/*     */         
/* 192 */         detalle.put("tipMonRefTipCambio", registro[14]);
/* 193 */         detalle.put("tipMonObjTipCambio", registro[15]);
/* 194 */         detalle.put("facTipCambio", registro[16]);
/* 195 */         detalle.put("fecTipCambio", registro[17]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 201 */         Percepcion.put("codigofacturadorSwf", codigoFacturadorSwf.toString());
/* 202 */         Percepcion.put("identificadorFirmaSwf", identificadorFirmaSwf);
/*     */         
/* 204 */         listaDetalle.add(detalle);
/*     */       } 
/*     */ 
/*     */       
/* 208 */       Percepcion.put("listaDetalle", listaDetalle);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 213 */     catch (IOException x) {
/* 214 */       throw new ParserException("No se pudo leer el archivo detalle: " + this.archivoDetalle, x);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 219 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Fin Procesamiento");
/*     */     
/* 221 */     return Percepcion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] parse(String templatesPath) throws ParserException {
/* 229 */     return parse(templatesPath, plantillaSeleccionada);
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\facturador\parser\pipe\PipePercepcionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
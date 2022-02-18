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
/*     */ public class PipeResumenBoletaParser
/*     */   extends PipeCpeAbstractParser
/*     */   implements Parser
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(PipeResumenBoletaParser.class);
/*     */ 
/*     */   
/*  29 */   private static String plantillaSeleccionada = "ConvertirRBoletasXML.ftl";
/*     */   
/*     */   private String archivoCabecera;
/*     */   
/*     */   private String archivoDetalleTributos;
/*     */   private String nombreArchivo;
/*     */   private Contribuyente contri;
/*     */   /*     */   
/*     */   public PipeResumenBoletaParser(Contribuyente contri, String[] archivos, String nombreArchivo) {
/*  38 */     this.contri = contri;
/*  39 */     this.nombreArchivo = nombreArchivo;
/*  40 */     this.archivoCabecera = archivos[0];
/*  41 */     this.archivoDetalleTributos = archivos[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> pipeToMap() throws ParserException {
/*  46 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Inicio Procesamiento");
/*     */     
/*  48 */     Integer error = new Integer(0);
/*     */ 
/*     */     
/*  51 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Leyendo Archivo: " + this.archivoCabecera);
/*  52 */     Map<String, Object> resumenGeneral = new HashMap<>();
/*  53 */     Map<String, Object> resumenDiario = null;
/*  54 */     List<Map<String, Object>> listaResumenDiario = new ArrayList<>();
/*     */     
/*  56 */     Path file = Paths.get(this.archivoCabecera, new String[0]);
/*  57 */     try(InputStream in = Files.newInputStream(file, new java.nio.file.OpenOption[0]); 
/*  58 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/*  59 */       String cadena = null;
/*     */       
/*  61 */       Integer linea = Integer.valueOf(0);
/*     */       
/*  63 */       while ((cadena = reader.readLine()) != null) {
/*  64 */         String[] registro = cadena.split("\\|");
/*     */         
/*  66 */         if (registro.length != 23 && error.intValue() == 0) {
/*  67 */           error = new Integer(1);
/*  68 */           throw new ParserException("El archivo RDI no contiene la cantidad de columnas esperada (23 columnas).");
/*     */         } 
/*     */         
/*  71 */         Integer integer1 = linea, integer2 = linea = Integer.valueOf(linea.intValue() + 1);
/*     */ 
/*     */         
/*  74 */         log.debug("SoftwareFacturadorController.formatoResumen...nombreArchivo: " + this.nombreArchivo);
/*  75 */         String[] idArchivo = this.nombreArchivo.split("\\-");
/*  76 */         String idResumen = idArchivo[1] + "-" + idArchivo[2] + "-" + idArchivo[3];
/*     */ 
/*     */         
/*  79 */         String fecEmision = registro[0];
/*  80 */         String fecResumen = registro[1];
/*     */         
/*  82 */         String tipDocResumen = registro[2];
/*  83 */         String idDocResumen = registro[3];
/*  84 */         String tipDocUsuario = registro[4];
/*  85 */         String numDocUsuario = registro[5];
/*  86 */         String tipMoneda = registro[6];
/*  87 */         String totValGrabado = registro[7];
/*  88 */         String totValExoneado = registro[8];
/*  89 */         String totValInafecto = registro[9];
/*  90 */         String totValExportado = registro[10];
/*  91 */         String totValGratuito = registro[11];
/*  92 */         String totOtroCargo = registro[12];
/*  93 */         String totImpCpe = registro[13];
/*  94 */         String tipDocModifico = registro[14];
/*  95 */         String serDocModifico = registro[15];
/*  96 */         String numDocModifico = registro[16];
/*  97 */         String tipRegPercepcion = registro[17];
/*  98 */         String porPercepcion = registro[18];
/*  99 */         String monBasePercepcion = registro[19];
/* 100 */         String monPercepcion = registro[20];
/* 101 */         String monTotIncPercepcion = registro[21];
/* 102 */         String tipEstado = registro[22];
/*     */         
/* 104 */         String identificadorFirmaSwf = "SIGN";
/* 105 */         Random calcularRnd = new Random();
/* 106 */         Integer codigoFacturadorSwf = Integer.valueOf((int)(calcularRnd.nextDouble() * 1000000.0D));
/*     */         
/* 108 */         if (linea.intValue() == 1) {
/* 109 */           resumenGeneral.put("nombreComercialSwf", this.contri.getNombreComercial());
/* 110 */           resumenGeneral.put("razonSocialSwf", this.contri.getRazonSocial());
/* 111 */           resumenGeneral.put("nroRucEmisorSwf", this.contri.getNumRuc());
/* 112 */           resumenGeneral.put("tipDocuEmisorSwf", "6");
/* 113 */           resumenGeneral.put("fechaEmision", fecEmision);
/* 114 */           resumenGeneral.put("fechaResumen", fecResumen);
/*     */           
/* 116 */           resumenGeneral.put("ublVersionIdSwf", "2.0");
/* 117 */           resumenGeneral.put("idResumen", idResumen);
/* 118 */           resumenGeneral.put("CustomizationIdSwf", "1.1");
/* 119 */           resumenGeneral.put("identificadorFacturadorSwf", "Elaborado por Sistema de Emision Electronica Facturador SUNAT (SEE-SFS) 1.3.2");
/*     */           
/* 121 */           resumenGeneral.put("codigoFacturadorSwf", codigoFacturadorSwf.toString());
/* 122 */           resumenGeneral.put("identificadorFirmaSwf", identificadorFirmaSwf);
/*     */           
/* 124 */           resumenDiario = new HashMap<>();
/* 125 */           resumenDiario.put("tipDocResumen", tipDocResumen);
/* 126 */           resumenDiario.put("idDocResumen", idDocResumen);
/* 127 */           resumenDiario.put("tipDocUsuario", tipDocUsuario);
/* 128 */           resumenDiario.put("numDocUsuario", numDocUsuario);
/* 129 */           resumenDiario.put("moneda", tipMoneda);
/* 130 */           resumenDiario.put("totValGrabado", totValGrabado);
/* 131 */           resumenDiario.put("totValExoneado", totValExoneado);
/* 132 */           resumenDiario.put("totValInafecto", totValInafecto);
/* 133 */           resumenDiario.put("totValExportado", totValExportado);
/* 134 */           resumenDiario.put("totValGratuito", totValGratuito);
/* 135 */           resumenDiario.put("totOtroCargo", totOtroCargo);
/* 136 */           resumenDiario.put("totImpCpe", totImpCpe);
/* 137 */           resumenDiario.put("tipDocModifico", tipDocModifico);
/* 138 */           resumenDiario.put("serDocModifico", serDocModifico);
/* 139 */           resumenDiario.put("numDocModifico", numDocModifico);
/* 140 */           resumenDiario.put("tipRegPercepcion", tipRegPercepcion);
/* 141 */           resumenDiario.put("porPercepcion", porPercepcion);
/* 142 */           resumenDiario.put("monBasePercepcion", monBasePercepcion);
/* 143 */           resumenDiario.put("monPercepcion", monPercepcion);
/* 144 */           resumenDiario.put("monTotIncPercepcion", monTotIncPercepcion);
/* 145 */           resumenDiario.put("tipEstado", tipEstado);
/* 146 */           resumenDiario.put("linea", linea);
/*     */         }
/*     */         else {
/*     */           
/* 150 */           resumenDiario = new HashMap<>();
/* 151 */           resumenDiario.put("tipDocResumen", tipDocResumen);
/* 152 */           resumenDiario.put("idDocResumen", idDocResumen);
/* 153 */           resumenDiario.put("tipDocUsuario", tipDocUsuario);
/* 154 */           resumenDiario.put("numDocUsuario", numDocUsuario);
/* 155 */           resumenDiario.put("moneda", tipMoneda);
/* 156 */           resumenDiario.put("totValGrabado", totValGrabado);
/* 157 */           resumenDiario.put("totValExoneado", totValExoneado);
/* 158 */           resumenDiario.put("totValInafecto", totValInafecto);
/* 159 */           resumenDiario.put("totValExportado", totValExportado);
/* 160 */           resumenDiario.put("totValGratuito", totValGratuito);
/* 161 */           resumenDiario.put("totOtroCargo", totOtroCargo);
/* 162 */           resumenDiario.put("totImpCpe", totImpCpe);
/* 163 */           resumenDiario.put("tipDocModifico", tipDocModifico);
/* 164 */           resumenDiario.put("serDocModifico", serDocModifico);
/* 165 */           resumenDiario.put("numDocModifico", numDocModifico);
/* 166 */           resumenDiario.put("tipRegPercepcion", tipRegPercepcion);
/* 167 */           resumenDiario.put("porPercepcion", porPercepcion);
/* 168 */           resumenDiario.put("monBasePercepcion", monBasePercepcion);
/* 169 */           resumenDiario.put("monPercepcion", monPercepcion);
/* 170 */           resumenDiario.put("monTotIncPercepcion", monTotIncPercepcion);
/* 171 */           resumenDiario.put("tipEstado", tipEstado);
/* 172 */           resumenDiario.put("linea", linea);
/*     */         } 
/*     */         
/* 175 */         listaResumenDiario.add(resumenDiario);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       resumenGeneral.put("listaResumen", listaResumenDiario);
/*     */     }
/* 183 */     catch (IOException x) {
/* 184 */       throw new ParserException("No se pudo leer el archivo cabecera: " + this.archivoCabecera, x);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 189 */     Path fileDetalle = Paths.get(this.archivoDetalleTributos, new String[0]);
/*     */     
/* 191 */     if (!Files.exists(fileDetalle, new java.nio.file.LinkOption[0]))
/*     */     {
/* 193 */       throw new ParserException("El archivo no existe: " + this.archivoDetalleTributos);
/*     */     }
/*     */ 
/*     */     
/* 197 */     List<Map<String, Object>> listaDetalle = new ArrayList<>();
/* 198 */     Map<String, Object> detalle = null;
/*     */     
/* 200 */     try(InputStream in = Files.newInputStream(fileDetalle, new java.nio.file.OpenOption[0]); 
/* 201 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/* 202 */       String cadena = null;
/*     */       
/* 204 */       Integer linea = Integer.valueOf(0);
/*     */       
/* 206 */       while ((cadena = reader.readLine()) != null) {
/* 207 */         String[] registro = cadena.split("\\|");
/* 208 */         if (registro.length != 6)
/*     */         {
/* 210 */           throw new ParserException("El archivo detalle no continene la cantidad de datos esperada (6 columnas).");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 215 */         detalle = new HashMap<>();
/* 216 */         detalle.put("idLineaRd", registro[0]);
/* 217 */         detalle.put("ideTributoRd", registro[1]);
/* 218 */         detalle.put("nomTributoRd", registro[2]);
/* 219 */         detalle.put("codTipTributoRd", registro[3]);
/* 220 */         detalle.put("mtoBaseImponibleRd", registro[4]);
/* 221 */         detalle.put("mtoTributoRd", registro[5]);
/*     */         
/* 223 */         listaDetalle.add(detalle);
/*     */       } 
/*     */ 
/*     */       
/* 227 */       resumenGeneral.put("listaTributosDocResumen", listaDetalle);
/*     */     }
/* 229 */     catch (IOException x) {
/* 230 */       throw new ParserException("No se pudo leer el archivo detalle: " + this.archivoDetalleTributos, x);
/*     */     } 
/*     */ 
/*     */     
/* 234 */     log.debug("SoftwareFacturadorController.formatoResumenBoletas...Fin Procesamiento");
/*     */     
/* 236 */     return resumenGeneral;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] parse(String templatesPath) throws ParserException {
/* 242 */     return parse(templatesPath, plantillaSeleccionada);
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\facturador\parser\pipe\PipeResumenBoletaParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
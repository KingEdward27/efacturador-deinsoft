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
/*     */ public class PipeNotaDebitoParser
/*     */   extends PipeCpeAbstractParser
/*     */   implements Parser
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(PipeNotaDebitoParser.class);
/*     */ 
/*     */   
/*  46 */   private static String plantillaSeleccionada = "ConvertirNDebitoXML.ftl";
/*     */ 
/*     */   
/*     */   private String archivoCabecera;
/*     */   
/*     */   private String archivoDetalle;
/*     */   
/*     */   private String nombreArchivo;
/*     */   
/*     */   private Contribuyente contri;
/*     */   
/*     */   private String archivoRelacionado;
/*     */   
/*     */   private String archivoAdiCabecera;
/*     */   
/*     */   private String archivoAdiDetalle;
/*     */   
/*     */   private String archivoLeyendas;
/*     */   
/*     */   private String archivoAdiTributos;
/*     */   
/*     */   private String archivoAdiVariableGlobal;
/*     */ 
/*     */   /*     */ 
/*     */   
/*     */   public PipeNotaDebitoParser(Contribuyente contri, String[] archivos, String nombreArchivo) {
/*  71 */     this.contri = contri;
/*  72 */     this.nombreArchivo = nombreArchivo;
/*     */     
/*  74 */     this.archivoCabecera = archivos[0];
/*  75 */     this.archivoDetalle = archivos[1];
/*  76 */     this.archivoRelacionado = archivos[2];
/*  77 */     this.archivoAdiCabecera = archivos[3];
/*  78 */     this.archivoAdiDetalle = archivos[4];
/*  79 */     this.archivoLeyendas = archivos[5];
/*  80 */     this.archivoAdiTributos = archivos[6];
/*  81 */     this.archivoAdiVariableGlobal = archivos[7];
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> pipeToMap() throws ParserException {
/*  86 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Inicio Procesamiento");
/*     */ 
/*     */     
/*  89 */     String[] datosArchivo = this.nombreArchivo.split("\\-");
/*     */     
/*  91 */     String identificadorFirmaSwf = "SIGN";
/*  92 */     Random calcularRnd = new Random();
/*  93 */     Integer codigoFacturadorSwf = Integer.valueOf((int)(calcularRnd.nextDouble() * 1000000.0D));
/*     */ 
/*     */     
/*  96 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Leyendo Archivo: " + this.archivoCabecera);
/*  97 */     Map<String, Object> notaDebito = new HashMap<>();
/*     */ 
/*     */     
/* 100 */     Path fileCabecera = Paths.get(this.archivoCabecera, new String[0]);
/*     */     
/* 102 */     if (!Files.exists(fileCabecera, new java.nio.file.LinkOption[0]))
/*     */     {
/* 104 */       throw new ParserException("El archivo no existe: " + this.archivoCabecera);
/*     */     }
/*     */     
/* 107 */     Integer error = new Integer(0);
/* 108 */     try(InputStream in = Files.newInputStream(fileCabecera, new java.nio.file.OpenOption[0]); 
/* 109 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/* 110 */       String cadena = null;
/*     */       
/* 112 */       while ((cadena = reader.readLine()) != null) {
/* 113 */         String[] registro = cadena.split("\\|");
/* 114 */         if (registro.length != 21 && error.intValue() == 0) {
/* 115 */           error = new Integer(1);
/* 116 */           throw new ParserException("El archivo cabecera no contiene la cantidad de datos esperada (21 columnas).");
/*     */         } 
/*     */         
/* 119 */         notaDebito = new HashMap<>();
/* 120 */         notaDebito.put("tipOperacion", registro[0]);
/* 121 */         notaDebito.put("fecEmision", registro[1]);
/* 122 */         notaDebito.put("horEmision", registro[2]);
/* 123 */         notaDebito.put("codLocalEmisor", registro[3]);
/* 124 */         notaDebito.put("tipDocUsuario", registro[4]);
/* 125 */         notaDebito.put("numDocUsuario", registro[5]);
/* 126 */         notaDebito.put("rznSocialUsuario", registro[6]);
/* 127 */         notaDebito.put("moneda", registro[7]);
/*     */         
/* 129 */         notaDebito.put("codMotivo", registro[8]);
/* 130 */         notaDebito.put("desMotivo", registro[9]);
/* 131 */         notaDebito.put("tipDocAfectado", registro[10]);
/* 132 */         notaDebito.put("numDocAfectado", registro[11]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 138 */         notaDebito.put("sumTotTributos", registro[12]);
/* 139 */         notaDebito.put("sumTotValVenta", registro[13]);
/* 140 */         notaDebito.put("sumPrecioVenta", registro[14]);
/* 141 */         notaDebito.put("sumDescTotal", registro[15]);
/* 142 */         notaDebito.put("sumOtrosCargos", registro[16]);
/* 143 */         notaDebito.put("sumTotalAnticipos", registro[17]);
/* 144 */         notaDebito.put("sumImpVenta", registro[18]);
/*     */         
/* 146 */         notaDebito.put("ublVersionId", registro[19]);
/* 147 */         notaDebito.put("customizationId", registro[20]);
/*     */ 
/*     */         
/* 150 */         notaDebito.put("ublVersionIdSwf", "2.0");
/* 151 */         notaDebito.put("CustomizationIdSwf", "1.0");
/* 152 */         notaDebito.put("nroCdpSwf", datosArchivo[2] + "-" + datosArchivo[3]);
/* 153 */         notaDebito.put("tipCdpSwf", datosArchivo[1]);
/*     */         
/* 155 */         notaDebito.put("nroRucEmisorSwf", this.contri.getNumRuc());
/* 156 */         notaDebito.put("tipDocuEmisorSwf", "6");
/* 157 */         notaDebito.put("nombreComercialSwf", this.contri.getNombreComercial());
/* 158 */         notaDebito.put("razonSocialSwf", this.contri.getRazonSocial());
/* 159 */         notaDebito.put("ubigeoDomFiscalSwf", this.contri.getDireccion().getUbigeo());
/* 160 */         notaDebito.put("direccionDomFiscalSwf", this.contri.getDireccion().getDireccion());
/* 161 */         notaDebito.put("deparSwf", this.contri.getDireccion().getDepar());
/* 162 */         notaDebito.put("provinSwf", this.contri.getDireccion().getProvin());
/* 163 */         notaDebito.put("distrSwf", this.contri.getDireccion().getDistr());
/* 164 */         notaDebito.put("urbanizaSwf", this.contri.getDireccion().getUrbaniza());
/* 165 */         notaDebito.put("paisDomFiscalSwf", "PE");
/* 166 */         notaDebito.put("codigoMontoDescuentosSwf", "2005");
/* 167 */         notaDebito.put("codigoMontoOperGravadasSwf", "1001");
/* 168 */         notaDebito.put("codigoMontoOperInafectasSwf", "1002");
/* 169 */         notaDebito.put("codigoMontoOperExoneradasSwf", "1003");
/* 170 */         notaDebito.put("idIgv", "1000");
/* 171 */         notaDebito.put("codIgv", "IGV");
/* 172 */         notaDebito.put("codExtIgv", "VAT");
/* 173 */         notaDebito.put("idIsc", "2000");
/* 174 */         notaDebito.put("codIsc", "ISC");
/* 175 */         notaDebito.put("codExtIsc", "EXC");
/* 176 */         notaDebito.put("idOtr", "9999");
/* 177 */         notaDebito.put("codOtr", "OTROS");
/* 178 */         notaDebito.put("codExtOtr", "OTH");
/* 179 */         notaDebito.put("tipoCodigoMonedaSwf", "01");
/* 180 */         notaDebito.put("identificadorFacturadorSwf", "Elaborado por Sistema de Emision Electronica Facturador SUNAT (SEE-SFS) 1.3.2");
/* 181 */         notaDebito.put("codigoFacturadorSwf", codigoFacturadorSwf.toString());
/* 182 */         notaDebito.put("identificadorFirmaSwf", identificadorFirmaSwf);
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 189 */     catch (IOException x) {
/* 190 */       throw new ParserException("No se pudo leer el archivo cabecera: " + this.archivoCabecera, x);
/*     */     } 
/*     */ 
/*     */     
/* 194 */     Path fileDetalle = Paths.get(this.archivoDetalle, new String[0]);
/*     */     
/* 196 */     if (!Files.exists(fileDetalle, new java.nio.file.LinkOption[0]))
/*     */     {
/* 198 */       throw new ParserException("El archivo no existe: " + this.archivoDetalle);
/*     */     }
/*     */ 
/*     */     
/* 202 */     List<Map<String, Object>> listaDetalle = new ArrayList<>();
/* 203 */     Map<String, Object> detalle = null;
/*     */     
/* 205 */     try(InputStream in = Files.newInputStream(fileDetalle, new java.nio.file.OpenOption[0]); 
/* 206 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/* 207 */       String cadena = null;
/*     */       
/* 209 */       Integer linea = Integer.valueOf(0);
/*     */       
/* 211 */       while ((cadena = reader.readLine()) != null) {
/* 212 */         String[] registro = cadena.split("\\|");
/*     */         
/* 214 */         if (registro.length != 36) {
/* 215 */           error = new Integer(1);
/* 216 */           throw new ParserException("El archivo detalle no contiene la cantidad de datos esperada (30 columnas).");
/*     */         } 
/*     */         
/* 219 */         linea = Integer.valueOf(linea.intValue() + 1);
/* 220 */         detalle = new HashMap<>();
/* 221 */         detalle.put("unidadMedida", registro[0]);
/* 222 */         detalle.put("ctdUnidadItem", registro[1]);
/* 223 */         detalle.put("codProducto", registro[2]);
/* 224 */         detalle.put("codProductoSUNAT", registro[3]);
/* 225 */         detalle.put("desItem", registro[4]);
/* 226 */         detalle.put("mtoValorUnitario", registro[5]);
/*     */         
/* 228 */         detalle.put("sumTotTributosItem", registro[6]);
/*     */         
/* 230 */         detalle.put("codTriIGV", registro[7]);
/* 231 */         detalle.put("mtoIgvItem", registro[8]);
/* 232 */         detalle.put("mtoBaseIgvItem", registro[9]);
/* 233 */         detalle.put("nomTributoIgvItem", registro[10]);
/* 234 */         detalle.put("codTipTributoIgvItem", registro[11]);
/* 235 */         detalle.put("tipAfeIGV", registro[12]);
/* 236 */         detalle.put("porIgvItem", registro[13]);
/*     */         
/* 238 */         detalle.put("codTriISC", registro[14]);
/* 239 */         detalle.put("mtoIscItem", registro[15]);
/* 240 */         detalle.put("mtoBaseIscItem", registro[16]);
/* 241 */         detalle.put("nomTributoIscItem", registro[17]);
/* 242 */         detalle.put("codTipTributoIscItem", registro[18]);
/* 243 */         detalle.put("tipSisISC", registro[19]);
/* 244 */         detalle.put("porIscItem", registro[20]);
/*     */         
/* 246 */         detalle.put("codTriOtro", registro[21]);
/* 247 */         detalle.put("mtoTriOtroItem", registro[22]);
/* 248 */         detalle.put("mtoBaseTriOtroItem", registro[23]);
/* 249 */         detalle.put("nomTributoOtroItem", registro[24]);
/* 250 */         detalle.put("codTipTributoOtroItem", registro[25]);
/* 251 */         detalle.put("porTriOtroItem", registro[26]);
/*     */         
/* 253 */         detalle.put("codTriIcbper", registro[27]);
/* 254 */         detalle.put("mtoTriIcbperItem", registro[28]);
/* 255 */         detalle.put("ctdBolsasTriIcbperItem", registro[29]);
/* 256 */         detalle.put("nomTributoIcbperItem", registro[30]);
/* 257 */         detalle.put("codTipTributoIcbperItem", registro[31]);
/* 258 */         detalle.put("mtoTriIcbperUnidad", registro[32]);
/*     */         
/* 260 */         detalle.put("mtoPrecioVentaUnitario", registro[33]);
/* 261 */         detalle.put("mtoValorVentaItem", registro[34]);
/*     */         
/* 263 */         detalle.put("mtoValorReferencialUnitario", registro[35]);
/*     */ 
/*     */         
/* 266 */         detalle.put("lineaSwf", linea);
/* 267 */         detalle.put("tipoCodiMoneGratiSwf", "02");
/*     */         
/* 269 */         listaDetalle.add(detalle);
/*     */       } 
/*     */ 
/*     */       
/* 273 */       notaDebito.put("listaDetalle", listaDetalle);
/*     */       
/* 275 */       formatoComunes(notaDebito, this.archivoRelacionado, this.archivoAdiCabecera, this.archivoAdiDetalle, this.archivoLeyendas, this.archivoAdiTributos, this.archivoAdiVariableGlobal);
/*     */ 
/*     */     
/*     */     }
/* 279 */     catch (IOException x) {
/* 280 */       throw new ParserException("No se pudo leer el archivo detalle: " + this.archivoDetalle, x);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 285 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Fin Procesamiento");
/*     */     
/* 287 */     return notaDebito;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] parse(String templatesPath) throws ParserException {
/* 295 */     return parse(templatesPath, plantillaSeleccionada);
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\facturador\parser\pipe\PipeNotaDebitoParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
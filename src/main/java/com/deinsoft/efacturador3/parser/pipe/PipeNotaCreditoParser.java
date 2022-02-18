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
/*     */ public class PipeNotaCreditoParser
/*     */   extends PipeCpeAbstractParser
/*     */   implements Parser
/*     */ {
/*  43 */   private static final Logger log = LoggerFactory.getLogger(PipeNotaCreditoParser.class);
/*     */ 
/*     */   
/*  46 */   private static String plantillaSeleccionada = "ConvertirNCreditoXML.ftl";
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
/*     */   public PipeNotaCreditoParser(Contribuyente contri, String[] archivos, String nombreArchivo) {
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
/*  97 */     Map<String, Object> notaCredito = new HashMap<>();
/*     */ 
/*     */     
/* 100 */     Path fileCabecera = Paths.get(this.archivoCabecera, new String[0]);
/*     */     
/* 102 */     if (!Files.exists(fileCabecera, new java.nio.file.LinkOption[0]))
/*     */     {
/* 104 */       throw new ParserException("El archivo no existe: " + this.archivoCabecera);
/*     */     }
/*     */ 
/*     */     
/* 108 */     try(InputStream in = Files.newInputStream(fileCabecera, new java.nio.file.OpenOption[0]); 
/* 109 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/* 110 */       String cadena = null;
/*     */       
/* 112 */       while ((cadena = reader.readLine()) != null) {
/* 113 */         String[] registro = cadena.split("\\|");
/* 114 */         if (registro.length != 21) {
/* 115 */           throw new ParserException("El archivo cabecera no continene la cantidad de datos esperada (21 columnas).");
/*     */         }
/*     */ 
/*     */         
/* 119 */         notaCredito = new HashMap<>();
/* 120 */         notaCredito.put("tipOperacion", registro[0]);
/* 121 */         notaCredito.put("fecEmision", registro[1]);
/* 122 */         notaCredito.put("horEmision", registro[2]);
/* 123 */         notaCredito.put("codLocalEmisor", registro[3]);
/* 124 */         notaCredito.put("tipDocUsuario", registro[4]);
/* 125 */         notaCredito.put("numDocUsuario", registro[5]);
/* 126 */         notaCredito.put("rznSocialUsuario", registro[6]);
/* 127 */         notaCredito.put("moneda", registro[7]);
/*     */         
/* 129 */         notaCredito.put("codMotivo", registro[8]);
/* 130 */         notaCredito.put("desMotivo", registro[9]);
/* 131 */         notaCredito.put("tipDocAfectado", registro[10]);
/* 132 */         notaCredito.put("numDocAfectado", registro[11]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 138 */         notaCredito.put("sumTotTributos", registro[12]);
/* 139 */         notaCredito.put("sumTotValVenta", registro[13]);
/* 140 */         notaCredito.put("sumPrecioVenta", registro[14]);
/* 141 */         notaCredito.put("sumDescTotal", registro[15]);
/* 142 */         notaCredito.put("sumOtrosCargos", registro[16]);
/* 143 */         notaCredito.put("sumTotalAnticipos", registro[17]);
/* 144 */         notaCredito.put("sumImpVenta", registro[18]);
/*     */         
/* 146 */         notaCredito.put("ublVersionId", registro[19]);
/* 147 */         notaCredito.put("customizationId", registro[20]);
/*     */ 
/*     */         
/* 150 */         notaCredito.put("ublVersionIdSwf", "2.0");
/* 151 */         notaCredito.put("CustomizationIdSwf", "1.0");
/* 152 */         notaCredito.put("nroCdpSwf", datosArchivo[2] + "-" + datosArchivo[3]);
/* 153 */         notaCredito.put("tipCdpSwf", datosArchivo[1]);
/* 154 */         notaCredito.put("nroRucEmisorSwf", this.contri.getNumRuc());
/* 155 */         notaCredito.put("tipDocuEmisorSwf", "6");
/* 156 */         notaCredito.put("nombreComercialSwf", this.contri.getNombreComercial());
/* 157 */         notaCredito.put("razonSocialSwf", this.contri.getRazonSocial());
/* 158 */         notaCredito.put("ubigeoDomFiscalSwf", this.contri.getDireccion().getUbigeo());
/* 159 */         notaCredito.put("direccionDomFiscalSwf", this.contri.getDireccion().getDireccion());
/* 160 */         notaCredito.put("deparSwf", this.contri.getDireccion().getDepar());
/* 161 */         notaCredito.put("provinSwf", this.contri.getDireccion().getProvin());
/* 162 */         notaCredito.put("distrSwf", this.contri.getDireccion().getDistr());
/* 163 */         notaCredito.put("urbanizaSwf", this.contri.getDireccion().getUrbaniza());
/* 164 */         notaCredito.put("paisDomFiscalSwf", "PE");
/* 165 */         notaCredito.put("codigoMontoDescuentosSwf", "2005");
/* 166 */         notaCredito.put("codigoMontoOperGravadasSwf", "1001");
/* 167 */         notaCredito.put("codigoMontoOperInafectasSwf", "1002");
/* 168 */         notaCredito.put("codigoMontoOperExoneradasSwf", "1003");
/* 169 */         notaCredito.put("idIgv", "1000");
/* 170 */         notaCredito.put("codIgv", "IGV");
/* 171 */         notaCredito.put("codExtIgv", "VAT");
/* 172 */         notaCredito.put("idIsc", "2000");
/* 173 */         notaCredito.put("codIsc", "ISC");
/* 174 */         notaCredito.put("codExtIsc", "EXC");
/* 175 */         notaCredito.put("idOtr", "9999");
/* 176 */         notaCredito.put("codOtr", "OTROS");
/* 177 */         notaCredito.put("codExtOtr", "OTH");
/* 178 */         notaCredito.put("tipoCodigoMonedaSwf", "01");
/* 179 */         notaCredito.put("identificadorFacturadorSwf", "Elaborado por Sistema de Emision Electronica Facturador SUNAT (SEE-SFS) 1.3.2");
/* 180 */         notaCredito.put("codigoFacturadorSwf", codigoFacturadorSwf.toString());
/* 181 */         notaCredito.put("identificadorFirmaSwf", identificadorFirmaSwf);
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/* 188 */     catch (IOException x) {
/* 189 */       throw new ParserException("No se pudo leer el archivo cabecera: " + this.archivoCabecera, x);
/*     */     } 
/*     */ 
/*     */     
/* 193 */     Path fileDetalle = Paths.get(this.archivoDetalle, new String[0]);
/*     */     
/* 195 */     if (!Files.exists(fileDetalle, new java.nio.file.LinkOption[0]))
/*     */     {
/* 197 */       throw new ParserException("El archivo no existe: " + this.archivoDetalle);
/*     */     }
/*     */ 
/*     */     
/* 201 */     List<Map<String, Object>> listaDetalle = new ArrayList<>();
/* 202 */     Map<String, Object> detalle = null;
/*     */     
/* 204 */     try(InputStream in = Files.newInputStream(fileDetalle, new java.nio.file.OpenOption[0]); 
/* 205 */         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
/* 206 */       String cadena = null;
/*     */       
/* 208 */       Integer linea = Integer.valueOf(0);
/*     */       
/* 210 */       while ((cadena = reader.readLine()) != null) {
/* 211 */         String[] registro = cadena.split("\\|");
/* 212 */         if (registro.length != 36) {
/* 213 */           throw new ParserException("El archivo detalle no continene la cantidad de datos esperada (30 columnas).");
/*     */         }
/*     */ 
/*     */         
/* 217 */         linea = Integer.valueOf(linea.intValue() + 1);
/* 218 */         detalle = new HashMap<>();
/* 219 */         detalle.put("unidadMedida", registro[0]);
/* 220 */         detalle.put("ctdUnidadItem", registro[1]);
/* 221 */         detalle.put("codProducto", registro[2]);
/* 222 */         detalle.put("codProductoSUNAT", registro[3]);
/* 223 */         detalle.put("desItem", registro[4]);
/* 224 */         detalle.put("mtoValorUnitario", registro[5]);
/*     */         
/* 226 */         detalle.put("sumTotTributosItem", registro[6]);
/*     */         
/* 228 */         detalle.put("codTriIGV", registro[7]);
/* 229 */         detalle.put("mtoIgvItem", registro[8]);
/* 230 */         detalle.put("mtoBaseIgvItem", registro[9]);
/* 231 */         detalle.put("nomTributoIgvItem", registro[10]);
/* 232 */         detalle.put("codTipTributoIgvItem", registro[11]);
/* 233 */         detalle.put("tipAfeIGV", registro[12]);
/* 234 */         detalle.put("porIgvItem", registro[13]);
/*     */         
/* 236 */         detalle.put("codTriISC", registro[14]);
/* 237 */         detalle.put("mtoIscItem", registro[15]);
/* 238 */         detalle.put("mtoBaseIscItem", registro[16]);
/* 239 */         detalle.put("nomTributoIscItem", registro[17]);
/* 240 */         detalle.put("codTipTributoIscItem", registro[18]);
/* 241 */         detalle.put("tipSisISC", registro[19]);
/* 242 */         detalle.put("porIscItem", registro[20]);
/*     */         
/* 244 */         detalle.put("codTriOtro", registro[21]);
/* 245 */         detalle.put("mtoTriOtroItem", registro[22]);
/* 246 */         detalle.put("mtoBaseTriOtroItem", registro[23]);
/* 247 */         detalle.put("nomTributoOtroItem", registro[24]);
/* 248 */         detalle.put("codTipTributoOtroItem", registro[25]);
/* 249 */         detalle.put("porTriOtroItem", registro[26]);
/*     */         
/* 251 */         detalle.put("codTriIcbper", registro[27]);
/* 252 */         detalle.put("mtoTriIcbperItem", registro[28]);
/* 253 */         detalle.put("ctdBolsasTriIcbperItem", registro[29]);
/* 254 */         detalle.put("nomTributoIcbperItem", registro[30]);
/* 255 */         detalle.put("codTipTributoIcbperItem", registro[31]);
/* 256 */         detalle.put("mtoTriIcbperUnidad", registro[32]);
/*     */         
/* 258 */         detalle.put("mtoPrecioVentaUnitario", registro[33]);
/* 259 */         detalle.put("mtoValorVentaItem", registro[34]);
/*     */         
/* 261 */         detalle.put("mtoValorReferencialUnitario", registro[35]);
/*     */ 
/*     */         
/* 264 */         detalle.put("lineaSwf", linea);
/* 265 */         detalle.put("tipoCodiMoneGratiSwf", "02");
/*     */ 
/*     */         
/* 268 */         listaDetalle.add(detalle);
/*     */       } 
/*     */ 
/*     */       
/* 272 */       notaCredito.put("listaDetalle", listaDetalle);
/*     */       
/* 274 */       formatoComunes(notaCredito, this.archivoRelacionado, this.archivoAdiCabecera, this.archivoAdiDetalle, this.archivoLeyendas, this.archivoAdiTributos, this.archivoAdiVariableGlobal);
/*     */ 
/*     */     
/*     */     }
/* 278 */     catch (IOException x) {
/* 279 */       throw new ParserException("No se pudo leer el archivo detalle: " + this.archivoDetalle, x);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 284 */     log.debug("SoftwareFacturadorController.formatoResumenBajas...Fin Procesamiento");
/*     */     
/* 286 */     return notaCredito;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] parse(String templatesPath) throws ParserException {
/* 294 */     return parse(templatesPath, plantillaSeleccionada);
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\facturador\parser\pipe\PipeNotaCreditoParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
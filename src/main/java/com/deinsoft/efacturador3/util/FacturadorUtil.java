/*     */ package com.deinsoft.efacturador3.util;
/*     */ 
import com.deinsoft.efacturador3.bean.Document;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import com.deinsoft.efacturador3.model.Documento;
import java.io.ByteArrayOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FacturadorUtil
/*     */ {
/*     */   public static String executeCommand(String command) throws Exception {
/*  23 */     StringBuffer output = new StringBuffer();
/*     */ 
/*     */     
/*  26 */     BufferedReader reader = null;
/*  27 */     Process p = Runtime.getRuntime().exec(command);
/*  28 */     p.waitFor();
/*  29 */     reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
/*     */     
/*  31 */     String line = "";
/*  32 */     while ((line = reader.readLine()) != null) {
/*  33 */       output.append(line + "\n");
/*     */     }
/*     */     
/*  36 */     reader.close();
/*     */     
/*  38 */     return output.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static InputStream comprimirArchivo(OutputStream salida,InputStream entrada, String nombre) throws Exception {
/*  43 */     byte[] buffer = new byte[1024];
/*     */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*  45 */     ZipOutputStream zos = new ZipOutputStream(bos);
/*  46 */     ZipEntry ze = new ZipEntry(nombre);
/*  47 */     zos.putNextEntry(ze);
/*     */     
/*     */     int len;
/*  50 */     while ((len = entrada.read(buffer)) > 0) {
/*  51 */       zos.write(buffer, 0, len);
/*     */     }
/*     */     
/*  54 */     entrada.close();
/*  55 */     zos.closeEntry();
/*     */     
/*  57 */     zos.close();
                return new ByteArrayInputStream(bos.toByteArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public static String convertirListaJson(List<Documento> listaConvertir) throws Exception {
/*  62 */     StringBuilder strListado = new StringBuilder();
/*     */     
/*  64 */     if (listaConvertir.size() > 0) {
/*     */       
/*  66 */       ObjectMapper mapper = new ObjectMapper();
/*  67 */       String lista = mapper.writeValueAsString(listaConvertir);
/*  68 */       strListado = new StringBuilder(lista);
/*     */     } else {
/*     */       
/*  71 */       strListado.setLength(0);
/*  72 */       strListado.append("{").append("\"sEcho\": 1,").append("\"iTotalRecords\": 0,")
/*  73 */         .append("\"iTotalDisplayRecords\": 0,").append("\"aaData\":[").append("]").append("}");
/*     */     } 
/*     */     
/*  76 */     return strListado.toString();
/*     */   }
/*     */   
/*     */   public static String obtenerCodigoError(String rutaArchivo, Integer lineaArchivo) throws Exception {
/*  80 */     String linea = "";
/*  81 */     Integer contador = Integer.valueOf(1);
/*  82 */     BufferedReader br = null;
/*     */     try {
/*  84 */       br = new BufferedReader(new FileReader(rutaArchivo));
/*  85 */       while ((linea = br.readLine()) != null && 
/*  86 */         contador.intValue() != lineaArchivo.intValue())
/*     */       {
/*  88 */         Integer integer1 = contador, integer2 = contador = Integer.valueOf(contador.intValue() + 1);
/*     */       }
/*  90 */       br.close();
/*  91 */     } catch (Exception e) {
/*  92 */       throw new Exception("Error en el utilitario obtenerLineaArchivo: " + e.getMessage());
/*     */     } 
/*     */     
/*  95 */     if (linea == null) {
/*  96 */       linea = "";
/*     */     }
/*  98 */     return linea;
/*     */   }
/*     */   
/*     */   public static String obtenerNumeroEnCadena(String mensaje) {
/* 102 */     Integer posicion = Integer.valueOf(mensaje.indexOf("errorCode"));
/* 103 */     if (posicion.intValue() > 0) {
/* 104 */       mensaje = mensaje.substring(posicion.intValue());
/*     */     }
/* 106 */     Integer largo = Integer.valueOf(mensaje.length());
/* 107 */     String numero = "";
/* 108 */     int endError = 0;
/* 109 */     for (int i = 0; i < largo.intValue() && 
/* 110 */       endError != 4; i++) {
/*     */       
/* 112 */       if (Character.isDigit(mensaje.charAt(i))) {
/* 113 */         numero = numero + mensaje.charAt(i);
/* 114 */         endError++;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 119 */     return numero;
/*     */   }
/*     */   
/*     */   public static Boolean esNumerico(String cadena) {
/* 123 */     Boolean retorno = Boolean.valueOf(cadena.matches("^[0-9]{1,2}$"));
/* 124 */     return retorno;
/*     */   }
/*     */   
/*     */   public static String completarCeros(String cadena, String lado, Integer cantidad) {
/* 128 */     String cadenaCompletada = "";
/*     */     
/* 130 */     if ("D".equals(lado)) {
/* 131 */       cadenaCompletada = String.format("%1$-" + cantidad + "s", new Object[] { cadena }).replace(" ", "0");
/*     */     } else {
/* 133 */       cadenaCompletada = String.format("%1$" + cantidad + "s", new Object[] { cadena }).replace(" ", "0");
/*     */     } 
/* 135 */     return cadenaCompletada;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void crearArchivoZip(String rutaArchivo, byte[] archivoZip) {
/*     */     try {
/* 142 */       ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(archivoZip));
/* 143 */       ZipEntry entry = null;
/* 144 */       FileOutputStream out = null;
/* 145 */       while ((entry = zipStream.getNextEntry()) != null) {
/*     */         
/* 147 */         String entryName = rutaArchivo + entry.getName();
/*     */         
/* 149 */         out = new FileOutputStream(entryName);
/*     */         
/* 151 */         byte[] byteBuff = new byte[4096];
/* 152 */         int bytesRead = 0;
/* 153 */         while ((bytesRead = zipStream.read(byteBuff)) != -1) {
/* 154 */           out.write(byteBuff, 0, bytesRead);
/*     */         }
/*     */         
/* 157 */         out.close();
/* 158 */         zipStream.closeEntry();
/*     */       } 
/* 160 */       zipStream.close();
/*     */     }
/* 162 */     catch (Exception e) {
/* 163 */       throw new RuntimeException("Error al crear archivo ZIP", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\sistema\facturado\\util\FacturadorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
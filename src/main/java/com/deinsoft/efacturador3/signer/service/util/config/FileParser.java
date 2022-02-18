/*    */ package com.deinsoft.efacturador3.signer.service.util.config;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileParser
/*    */ {
/*    */   public File getExistingReadableFile(String key, String value) {
/* 14 */     value = getFileName(key, value);
/* 15 */     File file = new File(value);
/* 16 */     if (!file.exists()) {
/* 17 */       throw new InvalidPropertyException(key, value, "existing file (doesn't exist)");
/*    */     }
/* 19 */     if (!file.canRead()) {
/* 20 */       throw new InvalidPropertyException(key, value, "readable file (can't read)");
/*    */     }
/* 22 */     return file;
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
/*    */ 
/*    */   
/*    */   public File getWriteableFile(String key, String value) {
/* 36 */     value = getFileName(key, value);
/* 37 */     File file = new File(value);
/*    */     
/*    */     try {
/* 40 */       if (!file.exists()) {
/*    */ 
/*    */ 
/*    */         
/* 44 */         File parentFile = file.getParentFile();
/* 45 */         if (parentFile != null && !parentFile.exists() && 
/* 46 */           !parentFile.mkdirs()) {
/* 47 */           throw new InvalidPropertyException(key, value, "writeable file (can't create directories '" + parentFile + "')");
/*    */         }
/*    */ 
/*    */         
/* 51 */         if (!file.createNewFile()) {
/* 52 */           throw new InvalidPropertyException(key, value, "writeable file (can't create)");
/*    */         }
/*    */       } 
/* 55 */       if (!file.canWrite()) {
/* 56 */         throw new InvalidPropertyException(key, value, "writeable file (can't write)");
/*    */       }
/* 58 */       return file;
/* 59 */     } catch (IOException e) {
/* 60 */       throw new InvalidPropertyException(key, value, "writeable file (I/O exception)");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getFileName(String key, String value) {
/* 66 */     if (containsStrangeWhiteSpace(value)) {
/* 67 */       throw new InvalidPropertyException(key, value, "writeable file (strange whitespace in name; wrong slashes used?)");
/*    */     }
/*    */     try {
/* 70 */       URL url = new URL(value);
/* 71 */       value = url.getFile().toString();
/* 72 */     } catch (MalformedURLException malformedURLException) {}
/*    */ 
/*    */ 
/*    */     
/* 76 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private boolean containsStrangeWhiteSpace(String fileName) {
/* 87 */     char[] cs = fileName.toCharArray();
/* 88 */     for (int i = 0; i < cs.length; i++) {
/* 89 */       if ("\t\n".indexOf(cs[i]) >= 0) {
/* 90 */         return true;
/*    */       }
/*    */     } 
/* 93 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\document\signer\servic\\util\config\FileParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
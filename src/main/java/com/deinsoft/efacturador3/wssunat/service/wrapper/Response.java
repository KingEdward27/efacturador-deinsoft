/*    */ package com.deinsoft.efacturador3.wssunat.service.wrapper;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Response
/*    */ {
/*    */   private int codigo;
/*    */   private boolean isError = false;
/*    */   private String mensaje;
/*    */   private List<String> warnings;
/*    */   /*    */   
/*    */   public Response(int codigo, String mensaje, List<String> warnings) {
/* 17 */     this.codigo = codigo;
/* 18 */     this.mensaje = mensaje;
/* 19 */     this.warnings = warnings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     if (codigo > 0 && (
/* 27 */       this.codigo < 100 || this.codigo > 399) && this.codigo <= 4000) {
/* 28 */       this.isError = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public int getCodigo() {
/* 33 */     return this.codigo;
/*    */   }
/*    */   public boolean isError() {
/* 36 */     return this.isError;
/*    */   }
/*    */   public String getMensaje() {
/* 39 */     return this.mensaje;
/*    */   }
/*    */   public List<String> getWarnings() {
/* 42 */     return this.warnings;
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\service\wrapper\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
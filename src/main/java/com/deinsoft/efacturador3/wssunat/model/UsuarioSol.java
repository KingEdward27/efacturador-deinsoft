/*    */ package com.deinsoft.efacturador3.wssunat.model;
/*    */ 
/*    */ public class UsuarioSol
/*    */ {
/*  5 */   public static ThreadLocal<UsuarioSol> usuarioThread = new ThreadLocal<>();
/*    */   
/*    */   private String numeroRUC;
/*    */   
/*    */   private String usuarioSOL;
/*    */   private String password;
/*    */   /*    */   
/*    */   public UsuarioSol(String numeroRUC, String usuarioSOL, String password) {
/* 13 */     this.numeroRUC = numeroRUC;
/* 14 */     this.usuarioSOL = usuarioSOL;
/* 15 */     this.password = password;
/* 16 */     usuarioThread.set(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getPassword() {
/* 25 */     return this.password;
/*    */   }
/*    */ 
/*    */   
/*    */   public String rucUsuarioSOL() {
/* 30 */     return this.numeroRUC + this.usuarioSOL;
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\model\UsuarioSol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
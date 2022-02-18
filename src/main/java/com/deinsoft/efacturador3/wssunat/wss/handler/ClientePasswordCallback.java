/*    */ package com.deinsoft.efacturador3.wssunat.wss.handler;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.security.auth.callback.Callback;
/*    */ import javax.security.auth.callback.CallbackHandler;
/*    */ import javax.security.auth.callback.UnsupportedCallbackException;
/*    */ import org.apache.wss4j.common.ext.WSPasswordCallback;
/*    */ import com.deinsoft.efacturador3.wssunat.model.UsuarioSol;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientePasswordCallback
/*    */   implements CallbackHandler
/*    */ {
/*    */   public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
/* 18 */     WSPasswordCallback pc = (WSPasswordCallback)callbacks[0];
/*    */     
/* 20 */     UsuarioSol usuarioSol = UsuarioSol.usuarioThread.get();
/*    */     try {
/* 22 */       pc.setPassword(usuarioSol.getPassword());
/* 23 */     } catch (Throwable t) {
/* 24 */       pc.setPassword("moddatos");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\wss\handler\ClientePasswordCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
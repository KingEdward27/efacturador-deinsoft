package com.deinsoft.efacturador3.wssunat.service.wrapper;

import com.deinsoft.efacturador3.wssunat.model.UsuarioSol;

public class ThreadUser
{
  private static ThreadLocal<UsuarioSol> usuarioSOL = new ThreadLocal<>();
  
  public static UsuarioSol get() {
    return usuarioSOL.get();
  }

  
  public static void initUser(UsuarioSol user) {
    usuarioSOL.set(user);
  }
}

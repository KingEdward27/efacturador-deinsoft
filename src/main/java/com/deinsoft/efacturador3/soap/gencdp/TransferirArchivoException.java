package com.deinsoft.efacturador3.soap.gencdp;

import javax.xml.ws.WebFault;

@WebFault(name = "ExceptionDetail", targetNamespace = "http://ws.registro.servicio2.sunat.gob.pe/")
public class TransferirArchivoException
  extends Exception
{
  private static final long serialVersionUID = -3499011408793858111L;
  private ExceptionDetail faultInfo;
  /*    */   
  public TransferirArchivoException(String message, ExceptionDetail faultInfo) {
    super(message);
    this.faultInfo = faultInfo;
  }

  public TransferirArchivoException(String message, ExceptionDetail faultInfo, Throwable cause) {
    super(message, cause);
    this.faultInfo = faultInfo;
  }




  
  public ExceptionDetail getFaultInfo() {
    return this.faultInfo;
  }
}

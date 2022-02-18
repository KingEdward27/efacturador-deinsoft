package com.deinsoft.efacturador3.wssunat.ws.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(name = "billService", targetNamespace = "http://service.sunat.gob.pe")
@XmlSeeAlso({ObjectFactory.class})
public interface BillService {
  @WebMethod(action = "urn:getStatus")
  @WebResult(name = "status", targetNamespace = "")
  @RequestWrapper(localName = "getStatusConsulta", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.facturador.ws.client.GetStatus")
  @ResponseWrapper(localName = "getStatusResponseConsulta", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.facturador.ws.client.GetStatusResponse")
  StatusResponse getStatus(@WebParam(name = "rucComprobante", targetNamespace = "") String paramString1, @WebParam(name = "tipoComprobante", targetNamespace = "") String paramString2, @WebParam(name = "serieComprobante", targetNamespace = "") String paramString3, @WebParam(name = "numeroComprobante", targetNamespace = "") Integer paramInteger);
  
  @WebMethod(action = "urn:getStatusCdr")
  @WebResult(name = "statusCdr", targetNamespace = "")
  @RequestWrapper(localName = "getStatusCdrConsulta", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.facturador.ws.client.GetStatusCdr")
  @ResponseWrapper(localName = "getStatusCdrResponseConsulta", targetNamespace = "http://service.sunat.gob.pe", className = "pe.gob.sunat.facturador.ws.client.GetStatusCdrResponse")
  StatusResponse getStatusCdr(@WebParam(name = "rucComprobante", targetNamespace = "") String paramString1, @WebParam(name = "tipoComprobante", targetNamespace = "") String paramString2, @WebParam(name = "serieComprobante", targetNamespace = "") String paramString3, @WebParam(name = "numeroComprobante", targetNamespace = "") Integer paramInteger);
}


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\ws\client\BillService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
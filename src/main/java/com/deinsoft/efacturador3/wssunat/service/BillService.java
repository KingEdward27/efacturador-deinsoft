package com.deinsoft.efacturador3.wssunat.service;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import com.deinsoft.efacturador3.wssunat.model.StatusResponse;

@WebService(targetNamespace = "http://service.sunat.gob.pe", name = "billService")
@XmlSeeAlso({ObjectFactory.class})
public interface BillService {
  @WebResult(name = "ticket", targetNamespace = "")
  @RequestWrapper(localName = "sendSummary", targetNamespace = "http://service.sunat.gob.pe", className = "com.deinsoft.efacturador2.wssunat.model.SendSummary")
  @WebMethod(action = "urn:sendSummary")
  @ResponseWrapper(localName = "sendSummaryResponse", targetNamespace = "http://service.sunat.gob.pe", className = "com.deinsoft.efacturador2.wssunat.model.SendSummaryResponse")
  String sendSummary(@WebParam(name = "fileName", targetNamespace = "") String paramString, @WebParam(name = "contentFile", targetNamespace = "") DataHandler paramDataHandler);
  
  @WebResult(name = "applicationResponse", targetNamespace = "")
  @RequestWrapper(localName = "sendBill", targetNamespace = "http://service.sunat.gob.pe", className = "com.deinsoft.efacturador2.wssunat.model.SendBill")
  @WebMethod(action = "urn:sendBill")
  @ResponseWrapper(localName = "sendBillResponse", targetNamespace = "http://service.sunat.gob.pe", className = "com.deinsoft.efacturador2.wssunat.model.SendBillResponse")
  byte[] sendBill(@WebParam(name = "fileName", targetNamespace = "") String paramString, @WebParam(name = "contentFile", targetNamespace = "") DataHandler paramDataHandler);
  
  @WebResult(name = "status", targetNamespace = "")
  @RequestWrapper(localName = "getStatus", targetNamespace = "http://service.sunat.gob.pe", className = "com.deinsoft.efacturador2.wssunat.model.GetStatus")
  @WebMethod(action = "urn:getStatus")
  @ResponseWrapper(localName = "getStatusResponse", targetNamespace = "http://service.sunat.gob.pe", className = "com.deinsoft.efacturador2.wssunat.model.GetStatusResponse")
  StatusResponse getStatus(@WebParam(name = "ticket", targetNamespace = "") String paramString);
  
  @WebResult(name = "ticket", targetNamespace = "")
  @RequestWrapper(localName = "sendPack", targetNamespace = "http://service.sunat.gob.pe", className = "com.deinsoft.efacturador2.wssunat.model.SendPack")
  @WebMethod(action = "urn:sendPack")
  @ResponseWrapper(localName = "sendPackResponse", targetNamespace = "http://service.sunat.gob.pe", className = "com.deinsoft.efacturador2.wssunat.model.SendPackResponse")
  String sendPack(@WebParam(name = "fileName", targetNamespace = "") String paramString, @WebParam(name = "contentFile", targetNamespace = "") DataHandler paramDataHandler);
}


/* Location:              D:\Proyectos\facturacion\EFacturador\java\SFS_v1.3.2\facturadorApp-1.3.2.jar!\pe\gob\sunat\facturaelectronica\service\BillService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
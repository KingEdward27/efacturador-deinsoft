package com.deinsoft.efacturador3.wssunat.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.deinsoft.efacturador3.wssunat.model.GetStatus;
import com.deinsoft.efacturador3.wssunat.model.GetStatusResponse;
import com.deinsoft.efacturador3.wssunat.model.SendBill;
import com.deinsoft.efacturador3.wssunat.model.SendBillResponse;
import com.deinsoft.efacturador3.wssunat.model.SendPack;
import com.deinsoft.efacturador3.wssunat.model.SendPackResponse;
import com.deinsoft.efacturador3.wssunat.model.SendSummary;
import com.deinsoft.efacturador3.wssunat.model.SendSummaryResponse;
import com.deinsoft.efacturador3.wssunat.model.StatusResponse;


















@XmlRegistry
public class ObjectFactory
{
  private static final QName _SendSummaryResponse_QNAME = new QName("http://service.sunat.gob.pe", "sendSummaryResponse");
  private static final QName _SendBillResponse_QNAME = new QName("http://service.sunat.gob.pe", "sendBillResponse");
  private static final QName _SendBill_QNAME = new QName("http://service.sunat.gob.pe", "sendBill");
  private static final QName _SendPackResponse_QNAME = new QName("http://service.sunat.gob.pe", "sendPackResponse");
  private static final QName _SendPack_QNAME = new QName("http://service.sunat.gob.pe", "sendPack");
  private static final QName _GetStatus_QNAME = new QName("http://service.sunat.gob.pe", "getStatus");
  private static final QName _GetStatusResponse_QNAME = new QName("http://service.sunat.gob.pe", "getStatusResponse");
  private static final QName _SendSummary_QNAME = new QName("http://service.sunat.gob.pe", "sendSummary");











  
  public GetStatus createGetStatus() {
    return new GetStatus();
  }




  
  public SendPack createSendPack() {
    return new SendPack();
  }




  
  public SendSummary createSendSummary() {
    return new SendSummary();
  }




  
  public GetStatusResponse createGetStatusResponse() {
    return new GetStatusResponse();
  }




  
  public SendPackResponse createSendPackResponse() {
    return new SendPackResponse();
  }




  
  public SendBillResponse createSendBillResponse() {
    return new SendBillResponse();
  }




  
  public SendBill createSendBill() {
    return new SendBill();
  }




  
  public SendSummaryResponse createSendSummaryResponse() {
    return new SendSummaryResponse();
  }




  
  public StatusResponse createStatusResponse() {
    return new StatusResponse();
  }




  
  @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendSummaryResponse")
  public JAXBElement<SendSummaryResponse> createSendSummaryResponse(SendSummaryResponse value) {
    return new JAXBElement<>(_SendSummaryResponse_QNAME, SendSummaryResponse.class, null, value);
  }




  
  @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendBillResponse")
  public JAXBElement<SendBillResponse> createSendBillResponse(SendBillResponse value) {
    return new JAXBElement<>(_SendBillResponse_QNAME, SendBillResponse.class, null, value);
  }




  
  @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendBill")
  public JAXBElement<SendBill> createSendBill(SendBill value) {
    return new JAXBElement<>(_SendBill_QNAME, SendBill.class, null, value);
  }




  
  @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendPackResponse")
  public JAXBElement<SendPackResponse> createSendPackResponse(SendPackResponse value) {
    return new JAXBElement<>(_SendPackResponse_QNAME, SendPackResponse.class, null, value);
  }




  
  @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendPack")
  public JAXBElement<SendPack> createSendPack(SendPack value) {
    return new JAXBElement<>(_SendPack_QNAME, SendPack.class, null, value);
  }




  
  @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "getStatus")
  public JAXBElement<GetStatus> createGetStatus(GetStatus value) {
    return new JAXBElement<>(_GetStatus_QNAME, GetStatus.class, null, value);
  }




  
  @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "getStatusResponse")
  public JAXBElement<GetStatusResponse> createGetStatusResponse(GetStatusResponse value) {
    return new JAXBElement<>(_GetStatusResponse_QNAME, GetStatusResponse.class, null, value);
  }




  
  @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendSummary")
  public JAXBElement<SendSummary> createSendSummary(SendSummary value) {
    return new JAXBElement<>(_SendSummary_QNAME, SendSummary.class, null, value);
  }
}

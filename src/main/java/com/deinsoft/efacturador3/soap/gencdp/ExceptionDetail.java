package com.deinsoft.efacturador3.soap.gencdp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExceptionDetail", propOrder = {"message"})
public class ExceptionDetail
{
  protected String message;
  
  public String getMessage() {
    return this.message;
  }
  
  public void setMessage(String value) {
    this.message = value;
  }

  public String toString() {
    return "ExceptionDetail [message=" + this.message + "]";
  }
}

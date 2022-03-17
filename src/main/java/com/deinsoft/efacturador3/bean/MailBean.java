/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.bean;

/**
 *
 * @author EDWARD-PC
 */
public class MailBean {
    private String asunto;
    private String contenido;
    private String correoElectronicoFrom;
    private String correoElectronicoTo;

    public MailBean() {

    }

    public MailBean(String asunto, String contenido, String correoElectronicoFrom, String correoElectronicoTo) {
        this.asunto = asunto;
        this.contenido = contenido;
        this.correoElectronicoFrom = correoElectronicoFrom;
        this.correoElectronicoTo = correoElectronicoTo;
    }
    
    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getCorreoElectronicoFrom() {
        return correoElectronicoFrom;
    }

    public void setCorreoElectronicoFrom(String correoElectronicoFrom) {
        this.correoElectronicoFrom = correoElectronicoFrom;
    }

    public String getCorreoElectronicoTo() {
        return correoElectronicoTo;
    }

    public void setCorreoElectronicoTo(String correoElectronicoTo) {
        this.correoElectronicoTo = correoElectronicoTo;
    }


    @Override
    public String toString() {
        return "MailBean [asunto=" + asunto + ", contenido=" + contenido
                + ", correoElectronicoFrom=" + correoElectronicoFrom + ", correoElectronicoTo= "+correoElectronicoFrom+"]";
    }
}

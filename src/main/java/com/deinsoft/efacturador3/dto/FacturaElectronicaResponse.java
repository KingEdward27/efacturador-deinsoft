/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author user
 */
public class FacturaElectronicaResponse {

    private String tipo;

    private String serie;

    private String numero;

    private LocalDate fechaEmision;
    private String status;
    private String observacion;

    public FacturaElectronicaResponse() {
    }

    public FacturaElectronicaResponse(String tipo, String serie, String numero, LocalDate fechaEmision,
                                      String status, String observacion) {
        this.tipo = tipo;
        this.serie = serie;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.status = status;
        this.observacion = observacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}

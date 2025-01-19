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
public class FacturaElectronicaDto {

    private String tipo;

    private String serie;

    private String numero;

    private LocalDate fechaEmision;
    private String ClienteDocumento;
    private String ClienteNombre;
    private BigDecimal sumatoriaIGV;
    private BigDecimal totalValorVentasGravadas;
    private BigDecimal totalValorVenta;

    private boolean exists;
    private boolean samePeriodo;
    private BigDecimal difIgv;
    private BigDecimal difTotales;

    public FacturaElectronicaDto() {
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

    public String getClienteDocumento() {
        return ClienteDocumento;
    }

    public void setClienteDocumento(String ClienteDocumento) {
        this.ClienteDocumento = ClienteDocumento;
    }

    public String getClienteNombre() {
        return ClienteNombre;
    }

    public void setClienteNombre(String ClienteNombre) {
        this.ClienteNombre = ClienteNombre;
    }

    public BigDecimal getSumatoriaIGV() {
        return sumatoriaIGV;
    }

    public void setSumatoriaIGV(BigDecimal sumatoriaIGV) {
        this.sumatoriaIGV = sumatoriaIGV;
    }

    public BigDecimal getTotalValorVentasGravadas() {
        return totalValorVentasGravadas;
    }

    public void setTotalValorVentasGravadas(BigDecimal totalValorVentasGravadas) {
        this.totalValorVentasGravadas = totalValorVentasGravadas;
    }

    public BigDecimal getTotalValorVenta() {
        return totalValorVenta;
    }

    public void setTotalValorVenta(BigDecimal totalValorVenta) {
        this.totalValorVenta = totalValorVenta;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public boolean isSamePeriodo() {
        return samePeriodo;
    }

    public void setSamePeriodo(boolean samePeriodo) {
        this.samePeriodo = samePeriodo;
    }

    public BigDecimal getDifIgv() {
        return difIgv;
    }

    public void setDifIgv(BigDecimal difIgv) {
        this.difIgv = difIgv;
    }

    public BigDecimal getDifTotales() {
        return difTotales;
    }

    public void setDifTotales(BigDecimal difTotales) {
        this.difTotales = difTotales;
    }

   
}

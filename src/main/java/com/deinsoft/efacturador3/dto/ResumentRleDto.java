/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.dto;

import java.math.BigDecimal;

/**
 *
 * @author user
 */
public class ResumentRleDto {
    private String tipo;
    private long total;
    private BigDecimal totalSubtotal;
    private BigDecimal totalIgv;
    private BigDecimal totalTotal;

    
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public BigDecimal getTotalSubtotal() {
        return totalSubtotal;
    }

    public void setTotalSubtotal(BigDecimal totalSubtotal) {
        this.totalSubtotal = totalSubtotal;
    }

    public BigDecimal getTotalIgv() {
        return totalIgv;
    }

    public void setTotalIgv(BigDecimal totalIgv) {
        this.totalIgv = totalIgv;
    }

    public BigDecimal getTotalTotal() {
        return totalTotal;
    }

    public void setTotalTotal(BigDecimal totalTotal) {
        this.totalTotal = totalTotal;
    }

    public ResumentRleDto() {
    }

    public ResumentRleDto(String tipo, long total, BigDecimal totalSubtotal, BigDecimal totalIgv, BigDecimal totalTotal) {
        this.tipo = tipo;
        this.total = total;
        this.totalSubtotal = totalSubtotal;
        this.totalIgv = totalIgv;
        this.totalTotal = totalTotal;
    }
    
    
}

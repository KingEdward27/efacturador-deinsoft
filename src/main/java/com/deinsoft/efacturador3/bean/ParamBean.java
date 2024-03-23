/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

/**
 *
 * @author EDWARD-PC
 */
public class ParamBean {
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaDesde;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaHasta;
    
    private String flagIsventa;
    
    private String direccion;
    
    private int flagEstado;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaVencimiento;
    
    public ParamBean() {
    }

    
    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getFlagIsventa() {
        return flagIsventa;
    }

    public void setFlagIsventa(String flagIsventa) {
        this.flagIsventa = flagIsventa;
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getFlagEstado() {
        return flagEstado;
    }

    public void setFlagEstado(int flagEstado) {
        this.flagEstado = flagEstado;
    }
    
    
}

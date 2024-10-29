/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.validacion;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author user
 */
public class PeriodoResponse {
    
    private String numEjercicio;

    private String desEstado;
    private List<PeriodoDetail> lisPeriodos;
//    
    public PeriodoResponse() {
    }

    public String getNumEjercicio() {
        return numEjercicio;
    }

    public void setNumEjercicio(String numEjercicio) {
        this.numEjercicio = numEjercicio;
    }

    public String getDesEstado() {
        return desEstado;
    }

    public void setDesEstado(String desEstado) {
        this.desEstado = desEstado;
    }

    public List<PeriodoDetail> getLisPeriodos() {
        return lisPeriodos;
    }

    public void setLisPeriodos(List<PeriodoDetail> lisPeriodos) {
        this.lisPeriodos = lisPeriodos;
    }

    

    
    
}

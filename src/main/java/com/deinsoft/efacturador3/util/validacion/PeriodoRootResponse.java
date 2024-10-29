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
public class PeriodoRootResponse {
    
    private List<PeriodoResponse> listPeriodos;

    public List<PeriodoResponse> getListPeriodos() {
        return listPeriodos;
    }

    public void setListPeriodos(List<PeriodoResponse> listPeriodos) {
        this.listPeriodos = listPeriodos;
    }

}

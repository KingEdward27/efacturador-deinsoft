/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.bean;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author EDWARD-PC
 */
public class ComprobanteTax {
    @NotBlank
    private String ide_tributo;
    
    @NotBlank
    private String nom_tributo;
    
    @NotBlank
    private String cod_tip_tributo;
    
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal mto_base_imponible;
    
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal mto_tributo;
    
    public ComprobanteTax() {
    }

    public String getIde_tributo() {
        return ide_tributo;
    }

    public void setIde_tributo(String ide_tributo) {
        this.ide_tributo = ide_tributo;
    }

    public String getNom_tributo() {
        return nom_tributo;
    }

    public void setNom_tributo(String nom_tributo) {
        this.nom_tributo = nom_tributo;
    }

    public String getCod_tip_tributo() {
        return cod_tip_tributo;
    }

    public void setCod_tip_tributo(String cod_tip_tributo) {
        this.cod_tip_tributo = cod_tip_tributo;
    }

    public BigDecimal getMto_base_imponible() {
        return mto_base_imponible;
    }

    public void setMto_base_imponible(BigDecimal mto_base_imponible) {
        this.mto_base_imponible = mto_base_imponible;
    }

    public BigDecimal getMto_tributo() {
        return mto_tributo;
    }

    public void setMto_tributo(BigDecimal mto_tributo) {
        this.mto_tributo = mto_tributo;
    }


    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author EDWARD-PC
 */
@Entity(name = "facturaElectronicaTax")
@Table(name = "factura_electronica_tax")
public class FacturaElectronicaTax {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "factura_electronica_tax_id", nullable = false, unique = true)
    private long id;
    
    @OneToOne
    @JoinColumn(name="factura_electronica_id")
    private FacturaElectronica facturaElectronica;
    
    @Column(name = "tax_id")
    private String taxId;
    
    @Column(name = "nom_tributo")
    private String nomTributo;
    
    @Column(name = "cod_tip_tributo")
    private String codTipTributo;
    
    @Column(name = "mto_base_imponible")
    private BigDecimal mtoBaseImponible;
    
    @Column(name = "taxtotal")
    private BigDecimal mtoTributo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FacturaElectronica getFacturaElectronica() {
        return facturaElectronica;
    }

    public void setFacturaElectronica(FacturaElectronica facturaElectronica) {
        this.facturaElectronica = facturaElectronica;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getNomTributo() {
        return nomTributo;
    }

    public void setNomTributo(String nomTributo) {
        this.nomTributo = nomTributo;
    }

    public String getCodTipTributo() {
        return codTipTributo;
    }

    public void setCodTipTributo(String codTipTributo) {
        this.codTipTributo = codTipTributo;
    }

    public BigDecimal getMtoBaseImponible() {
        return mtoBaseImponible;
    }

    public void setMtoBaseImponible(BigDecimal mtoBaseImponible) {
        this.mtoBaseImponible = mtoBaseImponible;
    }

    public BigDecimal getMtoTributo() {
        return mtoTributo;
    }

    public void setMtoTributo(BigDecimal mtoTributo) {
        this.mtoTributo = mtoTributo;
    }

    
    
}

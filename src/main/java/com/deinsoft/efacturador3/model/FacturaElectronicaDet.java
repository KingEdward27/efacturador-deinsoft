/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author EDWARD-PC
 */
@Entity(name = "facturaElectronicaDet")
@Table(name = "factura_electronica_det")
public class FacturaElectronicaDet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_act_invoice_id")
//    @SequenceGenerator(name = "seq_act_invoice_id", sequenceName = "seq_act_invoice_id", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "factura_electronica_det_id", nullable = false, unique = true)
    private long id;
    
    @OneToOne
    @JoinColumn(name="m_id")
    private FacturaElectronica facturaElectronica;
    
    @Column(name = "codigo")
    private String codigo;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;
    
    @Column(name = "precio_venta_unitario")
    private BigDecimal precioVentaUnitario;
    
    @Column(name = "afectacion_igv")
    private BigDecimal afectacionIgv;
    
    @Column(name = "unidad_medida")
    private String unidadMedida;
    
    @Column(name = "valor_venta_item")
    private BigDecimal valorVentaItem;
    
    @Column(name = "descuento")
    private BigDecimal descuento;
    
    @Column(name = "recargo")
    private BigDecimal recargo;
    
    @Column(name = "precio_Code")
    private String precioCode;
    
    @Column(name = "afectacion_IGVCode")
    private String afectacionIGVCode;

    @Column(name = "cod_tributo_igv")
    private String codTipTributoIgv;

    public FacturaElectronicaDet() {
    }

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


    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getPrecioVentaUnitario() {
        return precioVentaUnitario;
    }

    public void setPrecioVentaUnitario(BigDecimal precioVentaUnitario) {
        this.precioVentaUnitario = precioVentaUnitario;
    }

    public BigDecimal getAfectacionIgv() {
        return afectacionIgv;
    }

    public void setAfectacionIgv(BigDecimal afectacionIgv) {
        this.afectacionIgv = afectacionIgv;
    }


    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getValorVentaItem() {
        return valorVentaItem;
    }

    public void setValorVentaItem(BigDecimal valorVentaItem) {
        this.valorVentaItem = valorVentaItem;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public String getPrecioCode() {
        return precioCode;
    }

    public void setPrecioCode(String precioCode) {
        this.precioCode = precioCode;
    }

    public String getAfectacionIGVCode() {
        return afectacionIGVCode;
    }

    public void setAfectacionIGVCode(String afectacionIGVCode) {
        this.afectacionIGVCode = afectacionIGVCode;
    }

    public String getCodTipTributoIgv() {
        return codTipTributoIgv;
    }

    public void setCodTipTributoIgv(String codTipTributoIgv) {
        this.codTipTributoIgv = codTipTributoIgv;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    
    @Override
    public String toString() {
        return "com.deinsoft.efacturador3.bean.FacturaElectronicaDet[ facturaElectronicaDetPK=" + this.id + " ]";
    }

}

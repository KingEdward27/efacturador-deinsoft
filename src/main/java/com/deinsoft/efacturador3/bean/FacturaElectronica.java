/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author EDWARD-PC
 */
@Entity(name="facturaElectronica")
@Table(name = "factura_electronica")
public class FacturaElectronica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "m_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha_emision")
    private Date fechaEmision;
    
    private String tipo;
    
    private String serie;
    
    private String numero;
    
    private String fechaVencimiento;
    
    private String tipoOperacion;
    
    private String clienteTipo;
    
    private String clienteDocumento;
    
    private String clienteNombre;
    private String clienteDireccion;
    private String clienteEmail;
    private String clienteTelefono;
    private String vendedorNombre;
    private String observaciones;
    private String placaVehiculo;
    private String ordenCompra;
    private String guiaRemision;
    private String descuentoGlobalPorcentaje;
    
    
    private String moneda;
    private String notaTipo;
    private String notaMotivo;
    private String notaReferenciaTipo;
    private String notaReferenciaSerie;
    private String notaReferenciaNumero;
    private String incluirPdf;
    private String incluirXml;
    
    private BigDecimal sumatoriaIGV;
    private BigDecimal sumatoriaISC;
    private BigDecimal sumatoriaOtrosTributos;
    private BigDecimal sumatoriaOtrosCargos;
    private BigDecimal totalValorVentasGravadas;
    private BigDecimal totalValorVentasInafectas;
    private BigDecimal totalValorVentasExoneradas;
    
    @Column(name = "descuentos_globales")
    private BigDecimal descuentosGlobales;
    
    @Column(name = "importe_total")
    private BigDecimal importeTotal;
    
    @Column(name = "leyenda1")
    private String leyenda1;
    
    @Column(name = "leyenda2")
    private String leyenda2;
    
    @Column(name = "leyenda3")
    private String leyenda3;
    
    @Column(name = "xml_hash")
    private String xmlHash;
    
    @Column(name = "idempresa")
    private int idempresa;

    @Column(name = "total_valor_venta")
    private BigDecimal totalValorVenta;
    
    @Column(name = "customization_id")
    private String customizationId;
    
    @Column(name = "ind_situacion")
    private String indSituacion;
    
    private Date FechaGenXml;
//    @Column(name = "nombre_Archivo")
//    private String nombreArchivo;
    
    @OneToMany(mappedBy = "facturaElectronica",fetch=FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties(value = { "facturaElectronica" }, allowSetters = true)
    private List<FacturaElectronicaDet> listFacturaElectronicaDet;
    
    public void addFacturaElectronicaDet(FacturaElectronicaDet item){
	    item.setFacturaElectronica(this);
	}
    
    public FacturaElectronica() {
    }

    public FacturaElectronica(Long id) {
        this.id = id;
    }

    public FacturaElectronica(Long id, Date fechaEmision, String tipo, String serie, String numero, String fechaVencimiento, String tipoOperacion, String clienteTipo, String clienteDocumento, String clienteNombre, String clienteDireccion, String clienteEmail, String clienteTelefono, String vendedorNombre, String observaciones, String placaVehiculo, String ordenCompra, String guiaRemision, String descuentoGlobalPorcentaje, String moneda, String notaTipo, String notaMotivo, String notaReferenciaTipo, String notaReferenciaSerie, String notaReferenciaNumero, String incluirPdf, String incluirXml, BigDecimal sumatoriaIGV, BigDecimal sumatoriaISC, BigDecimal sumatoriaOtrosTributos, BigDecimal sumatoriaOtrosCargos, BigDecimal totalValorVentasGravadas, BigDecimal totalValorVentasInafectas, BigDecimal totalValorVentasExoneradas, BigDecimal descuentosGlobales, BigDecimal importeTotal, String leyenda1, String leyenda2, String leyenda3, String xmlHash, int idempresa, BigDecimal totalValorVenta, String customizationId, String indSituacion, List<FacturaElectronicaDet> listFacturaElectronicaDet) {
        this.id = id;
        this.fechaEmision = fechaEmision;
        this.tipo = tipo;
        this.serie = serie;
        this.numero = numero;
        this.fechaVencimiento = fechaVencimiento;
        this.tipoOperacion = tipoOperacion;
        this.clienteTipo = clienteTipo;
        this.clienteDocumento = clienteDocumento;
        this.clienteNombre = clienteNombre;
        this.clienteDireccion = clienteDireccion;
        this.clienteEmail = clienteEmail;
        this.clienteTelefono = clienteTelefono;
        this.vendedorNombre = vendedorNombre;
        this.observaciones = observaciones;
        this.placaVehiculo = placaVehiculo;
        this.ordenCompra = ordenCompra;
        this.guiaRemision = guiaRemision;
        this.descuentoGlobalPorcentaje = descuentoGlobalPorcentaje;
        this.moneda = moneda;
        this.notaTipo = notaTipo;
        this.notaMotivo = notaMotivo;
        this.notaReferenciaTipo = notaReferenciaTipo;
        this.notaReferenciaSerie = notaReferenciaSerie;
        this.notaReferenciaNumero = notaReferenciaNumero;
        this.incluirPdf = incluirPdf;
        this.incluirXml = incluirXml;
        this.sumatoriaIGV = sumatoriaIGV;
        this.sumatoriaISC = sumatoriaISC;
        this.sumatoriaOtrosTributos = sumatoriaOtrosTributos;
        this.sumatoriaOtrosCargos = sumatoriaOtrosCargos;
        this.totalValorVentasGravadas = totalValorVentasGravadas;
        this.totalValorVentasInafectas = totalValorVentasInafectas;
        this.totalValorVentasExoneradas = totalValorVentasExoneradas;
        this.descuentosGlobales = descuentosGlobales;
        this.importeTotal = importeTotal;
        this.leyenda1 = leyenda1;
        this.leyenda2 = leyenda2;
        this.leyenda3 = leyenda3;
        this.xmlHash = xmlHash;
        this.idempresa = idempresa;
        this.totalValorVenta = totalValorVenta;
        this.customizationId = customizationId;
        this.indSituacion = indSituacion;
        this.listFacturaElectronicaDet = listFacturaElectronicaDet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
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

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getClienteTipo() {
        return clienteTipo;
    }

    public void setClienteTipo(String clienteTipo) {
        this.clienteTipo = clienteTipo;
    }

    public String getClienteDocumento() {
        return clienteDocumento;
    }

    public void setClienteDocumento(String clienteDocumento) {
        this.clienteDocumento = clienteDocumento;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteDireccion() {
        return clienteDireccion;
    }

    public void setClienteDireccion(String clienteDireccion) {
        this.clienteDireccion = clienteDireccion;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public String getVendedorNombre() {
        return vendedorNombre;
    }

    public void setVendedorNombre(String vendedorNombre) {
        this.vendedorNombre = vendedorNombre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public String getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(String guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public String getDescuentoGlobalPorcentaje() {
        return descuentoGlobalPorcentaje;
    }

    public void setDescuentoGlobalPorcentaje(String descuentoGlobalPorcentaje) {
        this.descuentoGlobalPorcentaje = descuentoGlobalPorcentaje;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getNotaTipo() {
        return notaTipo;
    }

    public void setNotaTipo(String notaTipo) {
        this.notaTipo = notaTipo;
    }

    public String getNotaMotivo() {
        return notaMotivo;
    }

    public void setNotaMotivo(String notaMotivo) {
        this.notaMotivo = notaMotivo;
    }

    public String getNotaReferenciaTipo() {
        return notaReferenciaTipo;
    }

    public void setNotaReferenciaTipo(String notaReferenciaTipo) {
        this.notaReferenciaTipo = notaReferenciaTipo;
    }

    public String getNotaReferenciaSerie() {
        return notaReferenciaSerie;
    }

    public void setNotaReferenciaSerie(String notaReferenciaSerie) {
        this.notaReferenciaSerie = notaReferenciaSerie;
    }

    public String getNotaReferenciaNumero() {
        return notaReferenciaNumero;
    }

    public void setNotaReferenciaNumero(String notaReferenciaNumero) {
        this.notaReferenciaNumero = notaReferenciaNumero;
    }

    public String getIncluirPdf() {
        return incluirPdf;
    }

    public void setIncluirPdf(String incluirPdf) {
        this.incluirPdf = incluirPdf;
    }

    public String getIncluirXml() {
        return incluirXml;
    }

    public void setIncluirXml(String incluirXml) {
        this.incluirXml = incluirXml;
    }

    public BigDecimal getSumatoriaIGV() {
        return sumatoriaIGV;
    }

    public void setSumatoriaIGV(BigDecimal sumatoriaIGV) {
        this.sumatoriaIGV = sumatoriaIGV;
    }

    public BigDecimal getSumatoriaISC() {
        return sumatoriaISC;
    }

    public void setSumatoriaISC(BigDecimal sumatoriaISC) {
        this.sumatoriaISC = sumatoriaISC;
    }

    public BigDecimal getSumatoriaOtrosTributos() {
        return sumatoriaOtrosTributos;
    }

    public void setSumatoriaOtrosTributos(BigDecimal sumatoriaOtrosTributos) {
        this.sumatoriaOtrosTributos = sumatoriaOtrosTributos;
    }

    public BigDecimal getSumatoriaOtrosCargos() {
        return sumatoriaOtrosCargos;
    }

    public void setSumatoriaOtrosCargos(BigDecimal sumatoriaOtrosCargos) {
        this.sumatoriaOtrosCargos = sumatoriaOtrosCargos;
    }

    public BigDecimal getTotalValorVentasGravadas() {
        return totalValorVentasGravadas;
    }

    public void setTotalValorVentasGravadas(BigDecimal totalValorVentasGravadas) {
        this.totalValorVentasGravadas = totalValorVentasGravadas;
    }

    public BigDecimal getTotalValorVentasInafectas() {
        return totalValorVentasInafectas;
    }

    public void setTotalValorVentasInafectas(BigDecimal totalValorVentasInafectas) {
        this.totalValorVentasInafectas = totalValorVentasInafectas;
    }

    public BigDecimal getTotalValorVentasExoneradas() {
        return totalValorVentasExoneradas;
    }

    public void setTotalValorVentasExoneradas(BigDecimal totalValorVentasExoneradas) {
        this.totalValorVentasExoneradas = totalValorVentasExoneradas;
    }

    public BigDecimal getDescuentosGlobales() {
        return descuentosGlobales;
    }

    public void setDescuentosGlobales(BigDecimal descuentosGlobales) {
        this.descuentosGlobales = descuentosGlobales;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getLeyenda1() {
        return leyenda1;
    }

    public void setLeyenda1(String leyenda1) {
        this.leyenda1 = leyenda1;
    }

    public String getLeyenda2() {
        return leyenda2;
    }

    public void setLeyenda2(String leyenda2) {
        this.leyenda2 = leyenda2;
    }

    public String getLeyenda3() {
        return leyenda3;
    }

    public void setLeyenda3(String leyenda3) {
        this.leyenda3 = leyenda3;
    }

    public String getXmlHash() {
        return xmlHash;
    }

    public void setXmlHash(String xmlHash) {
        this.xmlHash = xmlHash;
    }

    public int getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(int idempresa) {
        this.idempresa = idempresa;
    }

    public BigDecimal getTotalValorVenta() {
        return totalValorVenta;
    }

    public void setTotalValorVenta(BigDecimal totalValorVenta) {
        this.totalValorVenta = totalValorVenta;
    }

    public String getCustomizationId() {
        return customizationId;
    }

    public void setCustomizationId(String customizationId) {
        this.customizationId = customizationId;
    }

    public String getIndSituacion() {
        return indSituacion;
    }

    public void setIndSituacion(String indSituacion) {
        this.indSituacion = indSituacion;
    }

    public Date getFechaGenXml() {
        return FechaGenXml;
    }

    public void setFechaGenXml(Date FechaGenXml) {
        this.FechaGenXml = FechaGenXml;
    }

    public List<FacturaElectronicaDet> getListFacturaElectronicaDet() {
        return listFacturaElectronicaDet;
    }

    public void setListFacturaElectronicaDet(List<FacturaElectronicaDet> listFacturaElectronicaDet) {
        this.listFacturaElectronicaDet = listFacturaElectronicaDet;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FacturaElectronica)) {
            return false;
        }
        FacturaElectronica other = (FacturaElectronica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.deinsoft.efacturador3.bean.FacturaElectronica[ mId=" + id + " ]";
    }
    
}
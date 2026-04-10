package com.deinsoft.efacturador3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "empresa_certificado")
public class EmpresaCertificado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", referencedColumnName = "idempresa")
    @JsonIgnoreProperties(value = {"listLocales", "hibernateLazyInitializer"}, allowSetters = true)
    private Empresa empresa;

    @Size(max = 300)
    @Column(name = "nombre")
    private String nombre;

    @Size(max = 300)
    @Column(name = "alias")
    private String alias;

    @Size(max = 300)
    @Column(name = "password")
    private String password;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inicio_vigencia")
    private Date fechaInicioVigencia;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_fin_vigencia")
    private Date fechaFinVigencia;

    @Size(max = 2000)
    @Column(name = "detalle", length = 2000)
    private String detalle;

    public EmpresaCertificado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public void setFechaInicioVigencia(Date fechaInicioVigencia) {
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public Date getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public void setFechaFinVigencia(Date fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "EmpresaCertificado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", alias='" + alias + '\'' +
                ", fechaInicioVigencia=" + fechaInicioVigencia +
                ", fechaFinVigencia=" + fechaFinVigencia +
                '}';
    }
}

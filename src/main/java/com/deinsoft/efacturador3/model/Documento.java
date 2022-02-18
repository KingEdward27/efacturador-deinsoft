/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author EDWARD-PC
 */
@Entity(name = "DOCUMENTO")
@Table(name = "DOCUMENTO")
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumentoPK documentoPK;
    
    @Column(name = "FEC_CARG")
    private String fecCarg;
    
    @Column(name = "FEC_GENE")
    private String fecGene;
    
    @Column(name = "FEC_ENVI")
    private String fecEnvi;
    
    @Column(name = "DES_OBSE")
    private String desObse;
    
    @Column(name = "NOM_ARCH")
    private String nomArch;
    
    @Column(name = "IND_SITU")
    private String indSitu;
    
    @Column(name = "TIP_ARCH")
    private String tipArch;
    
    @Column(name = "FIRM_DIGITAL")
    private String firmDigital;

    public Documento() {
    }

    public Documento(DocumentoPK documentoPK) {
        this.documentoPK = documentoPK;
    }

    public DocumentoPK getDocumentoPK() {
        return documentoPK;
    }

    public void setDocumentoPK(DocumentoPK documentoPK) {
        this.documentoPK = documentoPK;
    }

    public String getFecCarg() {
        return fecCarg;
    }

    public void setFecCarg(String fecCarg) {
        this.fecCarg = fecCarg;
    }

    public String getFecGene() {
        return fecGene;
    }

    public void setFecGene(String fecGene) {
        this.fecGene = fecGene;
    }

    public String getFecEnvi() {
        return fecEnvi;
    }

    public void setFecEnvi(String fecEnvi) {
        this.fecEnvi = fecEnvi;
    }

    public String getDesObse() {
        return desObse;
    }

    public void setDesObse(String desObse) {
        this.desObse = desObse;
    }

    public String getNomArch() {
        return nomArch;
    }

    public void setNomArch(String nomArch) {
        this.nomArch = nomArch;
    }

    public String getIndSitu() {
        return indSitu;
    }

    public void setIndSitu(String indSitu) {
        this.indSitu = indSitu;
    }

    public String getTipArch() {
        return tipArch;
    }

    public void setTipArch(String tipArch) {
        this.tipArch = tipArch;
    }

    public String getFirmDigital() {
        return firmDigital;
    }

    public void setFirmDigital(String firmDigital) {
        this.firmDigital = firmDigital;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoPK != null ? documentoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.documentoPK == null && other.documentoPK != null) || (this.documentoPK != null && !this.documentoPK.equals(other.documentoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.deinsoft.efacturador3.model.Documento[ documentoPK=" + documentoPK + " ]";
    }
    
}

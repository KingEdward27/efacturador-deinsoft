/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.model;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author EDWARD-PC
 */
@Embeddable
public class DocumentoPK implements Serializable{
    private String numRuc;
    private String tipDocu;
    private String numDocu;

    public DocumentoPK() {
    }

    public DocumentoPK(String numRuc, String tipDocu, String numDocu) {
        this.numRuc = numRuc;
        this.tipDocu = tipDocu;
        this.numDocu = numDocu;
    }

    
    public String getNumRuc() {
        return numRuc;
    }

    public void setNumRuc(String numRuc) {
        this.numRuc = numRuc;
    }

    public String getTipDocu() {
        return tipDocu;
    }

    public void setTipDocu(String tipDocu) {
        this.tipDocu = tipDocu;
    }

    public String getNumDocu() {
        return numDocu;
    }

    public void setNumDocu(String numDocu) {
        this.numDocu = numDocu;
    }
    
    
}

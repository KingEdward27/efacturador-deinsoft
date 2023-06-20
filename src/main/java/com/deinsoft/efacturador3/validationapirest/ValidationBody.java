/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.validationapirest;

import java.lang.reflect.Field;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author user
 */
public class ValidationBody {
    private String numRuc;
    private String codComp;
    private String numeroSerie;
    private String numero;
    private String fechaEmision;
    private double monto;

    public String getNumRuc() {
        return numRuc;
    }

    public void setNumRuc(String numRuc) {
        this.numRuc = numRuc;
    }

    public String getCodComp() {
        return codComp;
    }

    public void setCodComp(String codComp) {
        this.codComp = codComp;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
    
    public String toJson(ValidationBody c) throws IllegalArgumentException, IllegalAccessException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        for (Field f : c.getClass().getDeclaredFields()) {
            if (f.get(c) == null) {
                continue;
            }
            System.out.println(f.getGenericType() + " " + f.getName() + " = " + f.get(c));
            objectBuilder.add(f.getName(), f.get(c).toString());
        }

        JsonObject object = objectBuilder.build();
        return object.toString();
    }
}

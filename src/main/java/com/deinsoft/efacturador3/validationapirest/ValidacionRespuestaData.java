/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.validationapirest;

import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class ValidacionRespuestaData {

    private String estadoCp;
    private String estadoRuc;
    private String condDomiRuc;
    private ArrayList<String> observaciones;

    public String getEstadoCp() {
        return estadoCp;
    }

    public void setEstadoCp(String estadoCp) {
        this.estadoCp = estadoCp;
    }

    public String getEstadoRuc() {
        return estadoRuc;
    }

    public void setEstadoRuc(String estadoRuc) {
        this.estadoRuc = estadoRuc;
    }

    public String getCondDomiRuc() {
        return condDomiRuc;
    }

    public void setCondDomiRuc(String condDomiRuc) {
        this.condDomiRuc = condDomiRuc;
    }

    public ArrayList<String> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(ArrayList<String> observaciones) {
        this.observaciones = observaciones;
    }

    public ValidacionRespuestaData(String jsonString) {
        JSONObject object = new JSONObject(jsonString);
        if (object.length() > 0) {
            this.estadoCp = String.valueOf(object.get("estadoCp").toString());
            if (!this.estadoCp.equals("0")) {
                this.estadoRuc = String.valueOf(object.get("estadoRuc"));
                this.condDomiRuc = String.valueOf(object.get("condDomiRuc"));
//        this.observaciones = ArrayList.valueOf(object.get("observaciones").toString());
            }
        }

    }

}

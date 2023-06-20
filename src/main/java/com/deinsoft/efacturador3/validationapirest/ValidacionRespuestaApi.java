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
public class ValidacionRespuestaApi {
    private boolean success;
    private String message;
    private ValidacionRespuestaData data;
//    public String errMessage;
//    public String errCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidacionRespuestaData getData() {
        return data;
    }

    public void setData(ValidacionRespuestaData data) {
        this.data = data;
    }
    
    
    public ValidacionRespuestaApi(String jsonString,boolean success) {
        JSONObject object = new JSONObject(jsonString);
        this.success = success;
        if (success) {
            this.success = Boolean.valueOf(object.get("success").toString());
            this.message = String.valueOf(object.get("message"));
            this.data = new ValidacionRespuestaData(String.valueOf(object.get("data")));
            
        }
    }
}



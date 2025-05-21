/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.mail;

/**
 *
 * @author user
 */
public class EmailResponse {
    private boolean result;
    private String id;

    public EmailResponse() {
    }

    public EmailResponse(boolean result, String id) {
        this.result = result;
        this.id = id;
    }
    
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
}

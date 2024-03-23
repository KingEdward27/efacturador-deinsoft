/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.bean;

/**
 *
 * @author user
 */
public class Response {
    String code;
    String message;
    String detail;

    public Response() {
    }

    public Response(String code, String message, String detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
}

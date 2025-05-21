/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.mail;

/**
 *
 * @author user
 */
public class EmailRequestRoot {
    
    EmailRequest email;

    public EmailRequestRoot() {
    }

    public EmailRequestRoot(EmailRequest email) {
        this.email = email;
    }

    
    public EmailRequest getEmail() {
        return email;
    }

    public void setEmail(EmailRequest email) {
        this.email = email;
    }
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.mail;

import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class EmailRequest{
    public String html;
    public String text;
    public String subject;
    public Map<String,String> from;
    public List<Map<String,String>> to;
    public Map<String,String> attachments_binary;

    public EmailRequest() {
    }

    public EmailRequest(String html, String text, String subject, Map<String, String> from, List<Map<String, String>> to
            , Map<String, String> attachments_binary) {
        this.html = html;
        this.text = text;
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.attachments_binary = attachments_binary;
    }

    
    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, String> getFrom() {
        return from;
    }

    public void setFrom(Map<String, String> from) {
        this.from = from;
    }

    public List<Map<String, String>> getTo() {
        return to;
    }

    public void setTo(List<Map<String, String>> to) {
        this.to = to;
    }

    public Map<String, String> getAttachments_binary() {
        return attachments_binary;
    }

    public void setAttachments_binary(Map<String, String> attachments_binary) {
        this.attachments_binary = attachments_binary;
    }
    
    
    
}

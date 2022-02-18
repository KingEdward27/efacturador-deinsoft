/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.bean;

import javax.persistence.Id;

import org.bson.types.Binary;
/**
 *
 * @author EDWARD-PC
 */
@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document {

    private long m_id;
    
    @Id
    private String id;

    private String title;

    private Binary file;

    public Document() {
        super();
    }

    public Document(long m_id, String title) {
        super();
        this.m_id = m_id;
        this.title = title;
    }

    public long getM_id() {
        return m_id;
    }

    public void setM_id(long m_id) {
        this.m_id = m_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Binary getFile() {
        return file;
    }

    public void setFile(Binary file) {
        this.file = file;
    }

}

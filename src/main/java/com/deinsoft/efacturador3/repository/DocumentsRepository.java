/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.repository;
/**
 *
 * @author EDWARD-PC
 */
import com.deinsoft.efacturador3.bean.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentsRepository extends MongoRepository<Document,String>{

}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.bean.Document;
import com.deinsoft.efacturador3.repository.DocumentsRepository;
import com.deinsoft.efacturador3.service.DocumentService;
import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author EDWARD-PC
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentsRepository documentsRepository;

    public String addFile(Long idFactura, String title, byte[] bytes) throws IOException {
        Document fileToSave = new Document(idFactura, title);
        fileToSave.setFile(
                new Binary(BsonBinarySubType.BINARY, bytes));
        fileToSave = documentsRepository.insert(fileToSave);
        return fileToSave.getId();
    }

    public Document getFile(String id) {
        return documentsRepository.findById(id).get();
    }
}

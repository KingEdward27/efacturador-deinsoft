/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.deinsoft.efacturador3.service;

import com.deinsoft.efacturador3.bean.Document;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    public String addFile(Long idFactura, String title, byte[] bytes) throws IOException;

    public Document getFile(String id);
}

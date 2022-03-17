package com.deinsoft.efacturador3.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.deinsoft.efacturador3.model.Document;

public interface DocumentService {
	
	public String addFile(String title, byte[] bytes) throws IOException;

    public Document getFile(String id);
}

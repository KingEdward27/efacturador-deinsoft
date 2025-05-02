/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.service;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author user
 */
public interface FileStorageService {
   String storeFile(String path, MultipartFile file,String newFileName);
}

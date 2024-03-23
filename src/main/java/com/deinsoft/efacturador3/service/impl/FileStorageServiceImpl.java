/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.service.impl;

import com.deinsoft.efacturador3.config.AppConfig;
import com.deinsoft.efacturador3.service.FileStorageService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author user
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    AppConfig appConfig;
    
    @Autowired
    public FileStorageServiceImpl(AppConfig appConfig) {
        this.fileStorageLocation = Paths.get(appConfig.getRootPath()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create the directory where the upload files will be saved.", ex);
        }
    }
    
    @Override
    public String storeFile(String path, MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains valid  characters or not
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! File name which contains invalid path sequence " + fileName);
            }
            // Copy file to the target place (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(Paths.get(path + "/" + fileName));
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

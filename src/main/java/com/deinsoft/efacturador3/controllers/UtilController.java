/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.controllers;

import com.deinsoft.efacturador3.bean.ComprobanteCab;
import com.deinsoft.efacturador3.bean.MailBean;
import com.deinsoft.efacturador3.util.SendMail;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author EDWARD-PC
 */
@RestController
@RequestMapping("api/v1/util")
public class UtilController {
    
    @PostMapping(value = "/sendmail")
    public ResponseEntity<?> getDocuments(@Valid @RequestBody MailBean mailBean, 
            BindingResult bindingResult,HttpServletRequest request, HttpServletResponse response){
        String respuesta = SendMail.sendEmail(mailBean);
        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }
}

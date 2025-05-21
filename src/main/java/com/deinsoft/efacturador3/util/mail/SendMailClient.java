/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.mail;

import com.deinsoft.efacturador3.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.HttpMethod;

/**
 *
 * @author user
 */
public class SendMailClient {

    //get token
    private String url;
    private CredentialRequest credentialRequest;
    private final static ObjectWriter OW = new ObjectMapper().writer().withDefaultPrettyPrinter();
    
    public SendMailClient(String url, CredentialRequest credentialRequest) {
        this.url = url;
        this.credentialRequest = credentialRequest;
    }

    public EmailResponse send(EmailRequest emailRequest) throws JsonProcessingException {
        
        //get token
        String credentialJsonRequest = OW.writeValueAsString(credentialRequest);

        String credentialJsonResponse = Util.simpleApi(url+ "/oauth/access_token",
                credentialJsonRequest, HttpMethod.POST,
                "", "application/json", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        CredentialResponse credentialResponse = mapper.readValue(credentialJsonResponse, CredentialResponse.class);

        //prepare for send email
        String json = OW.writeValueAsString(
                new EmailRequestRoot(
                        new EmailRequest(emailRequest.getHtml(), "", emailRequest.getSubject(), emailRequest.getFrom(),
                        emailRequest.getTo(), emailRequest.getAttachments_binary())
                )
                
        );
        String mailJsonResult = Util.simpleApi(url+ "/smtp/emails", json, HttpMethod.POST,
                "Bearer " + credentialResponse.getAccesToken(), "application/json", "application/json");

        return mapper.readValue(mailJsonResult, EmailResponse.class);
    }

}

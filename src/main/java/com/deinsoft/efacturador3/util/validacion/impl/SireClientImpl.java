/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.validacion.impl;

import com.deinsoft.efacturador3.util.validacion.*;
import com.deinsoft.efacturador3.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author user
 */
public class SireClientImpl {

    //get token
    private final static ObjectWriter OW = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private String clientId;
    private String clientSecret;
    private String token;
    
    public SireClientImpl(String clientId, String clientSecret) throws Exception {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        getToken();
    }
    private String getToken() throws Exception{
       MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("scope", "https://api-sire.sunat.gob.pe");
//        body.add("client_id", "330c5519-61fc-40a9-af27-986b23220b3e");
//        body.add("client_secret", "h1/G6vS+YH2zk0waeQfI7A==");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        Map<String, Object> credentialMapResponse = Util.simpleApiWithFormBody(
                "https://api-seguridad.sunat.gob.pe/v1/clientessol/330c5519-61fc-40a9-af27-986b23220b3e/oauth2/token/",
                HttpMethod.POST, "", body);

        if (credentialMapResponse != null) {
            throw new Exception("Respuesta nula en servicio de autenticación");
        }
        token = credentialMapResponse.get("access_token").toString();
        return token; 
    }
    
    public PeriodoRootResponse getPeriodos() throws JsonProcessingException, Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object>  bodyPeriodos = Util.simpleApiWithFormBody(
                "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/padron/web/omisos/140000/periodos",
                HttpMethod.GET, getToken(), null);
        return mapper.readValue(bodyPeriodos.toString(), PeriodoRootResponse.class);
    }

}

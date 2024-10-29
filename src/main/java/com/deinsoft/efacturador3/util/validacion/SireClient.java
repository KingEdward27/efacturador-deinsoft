/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.validacion;

import com.deinsoft.efacturador3.util.GenericHttpClient;
import com.deinsoft.efacturador3.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.base.Splitter;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author user
 */
public class SireClient {

    //get token
    private final static ObjectWriter OW = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;
    private String token;

    public SireClient(String clientId, String clientSecret, String username, String password) throws Exception {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
        getToken0();
    }

    private String getToken0() {
        GenericHttpClient client = new GenericHttpClient();
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "password");
        body.put("scope", "https://api-sire.sunat.gob.pe");
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("username", username);
        body.put("password", password);
        
        String url = "https://api-seguridad.sunat.gob.pe/v1/clientessol/330c5519-61fc-40a9-af27-986b23220b3e/oauth2/token/";
        
        Map<String, String> headers = new HashMap<>();

        try {
            HttpResponse<String> response = client.sendRequestFormBody("POST", url, headers, body, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            
            Map<String,Object> result =
                    new ObjectMapper().readValue(response.body(), HashMap.class);
            
            token = result.get("access_token").toString();
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getToken() throws Exception {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("scope", "https://api-sire.sunat.gob.pe");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);

        Map<String, Object> credentialMapResponse = Util.simpleApiWithFormBody(
                "https://api-seguridad.sunat.gob.pe/v1/clientessol/330c5519-61fc-40a9-af27-986b23220b3e/oauth2/token/",
                HttpMethod.POST, "", body);

        if (credentialMapResponse == null) {
            throw new Exception("Respuesta nula en servicio de autenticación");
        }
        token = credentialMapResponse.get("access_token").toString();
        return token;
    }

    public List<PeriodoResponse> getPeriodos() throws JsonProcessingException, Exception {
        ObjectMapper mapper = new ObjectMapper();
        GenericHttpClient client = new GenericHttpClient();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        HttpResponse<String> bodyPeriodos = client.sendRequestJsonBody(
                HttpMethod.GET.toString(),"https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/padron/web/omisos/140000/periodos",
                headers,
                 "",HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(bodyPeriodos.body(), new TypeReference<List<PeriodoResponse>>() {});
        //return mapper.readValue(bodyPeriodos.body(), PeriodoRootResponse.class);
    }

}

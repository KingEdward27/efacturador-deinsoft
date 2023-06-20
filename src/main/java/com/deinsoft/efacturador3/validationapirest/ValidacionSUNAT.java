/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.validationapirest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.conn.ssl.TrustStrategy;

/**
 *
 * @author user
 */
public class ValidacionSUNAT {

    String urlToken = "";
    String urlValidation = "";
//    static final String URL_TOKEN = "https://api-seguridad.sunat.gob.pe/v1/clientesextranet/815458f9-9a3d-4fe7-8135-ce68f2aa9ed6/oauth2/token/";
//    static final String URL_VALIDATION = "https://api.sunat.gob.pe/v1/contribuyente/contribuyentes/10414316595/validarcomprobante";
    
    public ValidacionSUNAT(String urlToken, String urlValidation){
        this.urlToken = urlToken;
        this.urlValidation = urlValidation;
    }
    
    public String getApiToken(String client_id, String client_secret) throws Exception {
        Map<String, Object> respuesta = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Authorization", token);
//        headers.add("Content-Type", "application/json");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(this.urlToken)
//                .queryParam("client_id", "{client_id}")
//                .queryParam("client_secret", "{client_secret}")
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
//        params.put("grant_type", "client_credentials");
//        params.put("scope", "https://api.sunat.gob.pe/v1/contribuyente/contribuyentes");
//        params.put("client_id", client_id);
//        params.put("client_secret", client_secret);
        
//        Map<String, String> params = new HashMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "https://api.sunat.gob.pe/v1/contribuyente/contribuyentes");
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        

        HttpEntity<MultiValueMap<String, String>> entityReq = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = getRestTemplate().exchange(urlTemplate,
                HttpMethod.POST, entityReq,
                Map.class, params);
        
        if (response.hasBody() && (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED)) {
            respuesta = response.getBody();
            if (respuesta != null) {
                System.out.println(respuesta.get("access_token"));
//                this.logger
//                        .info("TokenResponse -> " + " - TokenType: " + tokenResponse.getTokenType() + " - ConsentedOn: "
//                                + tokenResponse.getConsentedOn() + " - ExpiresIn: " + tokenResponse.getExpiresIn()
//                                + " - RefreshTokenExpiresin: " + tokenResponse.getRefreshTokenExpiresIn());
                return respuesta.get("access_token").toString();
            } else {
                String msg = "Respuesta vacía del API";
//                this.logger.warning(msg);
                throw new Exception(msg);
            }
        } else {
            System.out.println(response.getBody());
            String msg = "Llamada fallida al API, HttpStatus: " + response.getStatusCode().value();
//            this.logger.warning(msg);
            
            throw new Exception(msg);
        }
    }

    public ValidacionRespuestaApi validate(String token, String jsonBody) {
        boolean result = false;
        ValidacionRespuestaApi respuesta = null;
        System.out.println("jsonBody: "+jsonBody);
        try {
            URL url = new URL(this.urlValidation);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            writer.write(jsonBody);
            writer.close();

            BufferedReader br = null;
            if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                result = true;
            } else {
                br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                result = false;
            }
            String output, jsonString = "";
            System.out.println("output is-----------------");

            while ((output = br.readLine()) != null) {
                System.out.println(output);
                jsonString = jsonString + output;
            }
            respuesta = new ValidacionRespuestaApi(jsonString, result);

            return respuesta;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//    public static Map<String, Object> getApiValidation(String token, String client_secret) throws Exception {
//        Map<String, Object> respuesta = null;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.add("Authorization", token);
////        headers.add("Content-Type", "application/json");
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//
//        String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://api-seguridad.sunat.gob.pe/v1/clientesextranet/815458f9-9a3d-4fe7-8135-ce68f2aa9ed6/oauth2/token/")
////                .queryParam("client_id", "{client_id}")
////                .queryParam("client_secret", "{client_secret}")
//                .encode()
//                .toUriString();
//
//        Map<String, String> params = new HashMap<>();
//        params.put("client_id", client_id);
//        params.put("client_secret", client_secret);
//
//        HttpEntity<MultiValueMap<String, String>> entityReq = new HttpEntity<>(body, headers);
//        ResponseEntity<Map> response = getRestTemplate().exchange(urlTemplate,
//                HttpMethod.POST, entityReq,
//                Map.class, params);
//        if (response.hasBody() && (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED)) {
//            respuesta = response.getBody();
//            if (respuesta != null) {
////                this.logger
////                        .info("TokenResponse -> " + " - TokenType: " + tokenResponse.getTokenType() + " - ConsentedOn: "
////                                + tokenResponse.getConsentedOn() + " - ExpiresIn: " + tokenResponse.getExpiresIn()
////                                + " - RefreshTokenExpiresin: " + tokenResponse.getRefreshTokenExpiresIn());
//                return respuesta;
//            } else {
//                String msg = "Respuesta vacía del API";
////                this.logger.warning(msg);
//                throw new Exception(msg);
//            }
//        } else {
//            String msg = "Llamada fallida al API, HttpStatus: " + response.getStatusCode().value();
////            this.logger.warning(msg);
//            throw new Exception(msg);
//        }
//    }
    
    private RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {

            @Override
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        };
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(5000);
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(10000);
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }
}

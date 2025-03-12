/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.SSLContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
/**
 *
 * @author user
 */
public class GenericRestClient {

    private final RestTemplate restTemplate;

    public GenericRestClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.restTemplate = getRestTemplate();
    }

    /**
     * Método genérico para realizar solicitudes HTTP.
     *
     * @param url          La URL del endpoint.
     * @param method       El método HTTP (GET, POST, PUT, DELETE).
     * @param headers      Headers de la solicitud.
     * @param requestBody  Cuerpo de la solicitud (puede ser null).
     * @param responseType El tipo de respuesta esperada.
     * @param <T>          Tipo genérico para la respuesta.
     * @return La respuesta deserializada en el tipo especificado.
     */
    public <T> T sendRequest(
            String url,
            HttpMethod method,
            Map<String, String> headers,
            Object requestBody,
            ParameterizedTypeReference<T> responseType) {

        // Configurar los headers
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }

        // Crear el cuerpo de la solicitud
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, httpHeaders);

        // Realizar la solicitud
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                method,
                entity,
                responseType
        );

        // Devolver el cuerpo de la respuesta
        return response.getBody();
    }
    
    /**
     * Método genérico para realizar solicitudes HTTP.
     *
     * @param url          La URL del endpoint.
     * @param method       El método HTTP (GET, POST, PUT, DELETE).
     * @param headers      Headers de la solicitud.
     * @param requestBody  Cuerpo de la solicitud (puede ser null).
     * @param responseType El tipo de respuesta esperada.
     * @param <T>          Tipo genérico para la respuesta.
     * @return La respuesta deserializada en el tipo especificado.
     */
    public <T> T sendRequestFormBody(
            String url,
            HttpMethod method,
            Map<String, String> headers,
            MultiValueMap<String, String> formBody,
            ParameterizedTypeReference<T> responseType) throws Exception {

        // Configurar los headers
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }

        // Crear el cuerpo de la solicitud
        HttpEntity<Object> entity = new HttpEntity<>(formBody, httpHeaders);

        // Realizar la solicitud
        ResponseEntity<T> response = restTemplate.exchange(
                url,
                method,
                entity,
                responseType
        );

        if (response.hasBody() && (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED)) {
            
            
                
            if (response == null) {
                String msg = "Respuesta vacía del API";
//                this.logger.warning(msg);
                throw new Exception(msg);
            }
            return response.getBody();
        } else {
            System.out.println(response.getBody());
            String msg = "Llamada fallida al API, HttpStatus: " + response.getStatusCode().value();
//            this.logger.warning(msg);

            throw new Exception(msg);
        }
    }
    
    private static RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
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

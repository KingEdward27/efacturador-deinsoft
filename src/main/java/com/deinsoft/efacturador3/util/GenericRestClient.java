/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util;

import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
/**
 *
 * @author user
 */
public class GenericRestClient {

    private final RestTemplate restTemplate;

    public GenericRestClient() {
        this.restTemplate = new RestTemplate();
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
}

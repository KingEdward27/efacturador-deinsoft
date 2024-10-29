/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

public class GenericHttpClient {

    private final HttpClient client;

    public GenericHttpClient() {
        this.client = HttpClient.newHttpClient();
    }

//    public <T> HttpResponse<T> sendRequest(String method, String url,
//            Map<String, String> headers,
//            String contentType,
//            Map<String, String> formParams,
//            String jsonBody,
//            HttpResponse.BodyHandler<T> responseHandler) throws Exception {
//
//        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
//                .uri(new URI(url))
//                .method(method.toUpperCase(), buildBody(contentType, formParams, jsonBody));
//
//        // Agregar headers
//        if (headers != null) {
//            headers.forEach(requestBuilder::header);
//        }
//        if (contentType != null) {
//            requestBuilder.header("Content-Type", contentType);
//        }
//
//        HttpRequest request = requestBuilder.build();
//        return client.send(request, responseHandler);
//    }

    public <T> HttpResponse<T> sendRequestJsonBody(String method, String url,
            Map<String, String> headers,
            String jsonBody,
            HttpResponse.BodyHandler<T> responseHandler) throws Exception {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .method(method.toUpperCase(), buildBody(MediaType.APPLICATION_JSON.toString(), null, jsonBody));

        // Agregar headers
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        requestBuilder.header("Content-Type", MediaType.APPLICATION_JSON.toString());

        HttpRequest request = requestBuilder.build();
        return client.send(request, responseHandler);
    }

    public <T> HttpResponse<T> sendRequestFormBody(String method, String url,
            Map<String, String> headers,
            Map<String, String> formParams,
            HttpResponse.BodyHandler<T> responseHandler) throws Exception {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(new URI(url))
                .method(method.toUpperCase(), 
                        buildBody(MediaType.APPLICATION_FORM_URLENCODED.toString(), formParams, null));

        // Agregar headers
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        requestBuilder.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        
        HttpRequest request = requestBuilder.build();
        return client.send(request, responseHandler);
    }

    private BodyPublisher buildBody(String contentType, Map<String, String> formParams, String jsonBody) {
        if (contentType == null) {
            return BodyPublishers.noBody();
        }

        switch (contentType) {
            case "application/json":
                return BodyPublishers.ofString(jsonBody != null ? jsonBody : "");
            case "application/x-www-form-urlencoded":
                String formUrlEncodedBody = formParams != null ? formParams.entrySet()
                        .stream()
                        .map(e -> String.join("=",
                        URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8),
                        URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8)))
                        .collect(Collectors.joining("&"))
                        : "";
                return BodyPublishers.ofString(formUrlEncodedBody, StandardCharsets.UTF_8);
            case "multipart/form-data":
                // Este es un caso más complejo, en un entorno real necesitarías manejar los límites de form-data.
                throw new UnsupportedOperationException("Multipart/form-data aún no soportado.");
            default:
                return BodyPublishers.noBody();
        }
    }
}

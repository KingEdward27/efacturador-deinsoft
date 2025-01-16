/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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

//    public Object sendRequest(String method, String url, Map<String, String> headers, 
//                              String contentType, Map<String, String> formParams, 
//                              String jsonBody, String outputZipFilePath) throws Exception {
//        // Crear la URL y la conexión
//        URL urlObj = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
//
//        // Establecer el método HTTP
//        connection.setRequestMethod(method.toUpperCase());
//
//        // Agregar headers
//        if (headers != null) {
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                connection.setRequestProperty(entry.getKey(), entry.getValue());
//            }
//        }
//
//        // Establecer el tipo de contenido
//        if (contentType != null) {
//            connection.setRequestProperty("Content-Type", contentType);
//        }
//
//        // Manejo del cuerpo de la solicitud (POST, PUT)
//        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
//            connection.setDoOutput(true);
//            String requestBody = buildRequestBody(contentType, formParams, jsonBody);
//            if (requestBody != null) {
//                try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
//                    dos.writeBytes(requestBody);
//                    dos.flush();
//                }
//            }
//        }
//
//        // Leer la respuesta
//        int responseCode = connection.getResponseCode();
//        String responseContentType = connection.getHeaderField("Content-Type");
//
//        // Si la respuesta es un archivo ZIP
//        if (responseContentType != null && responseContentType.contains("application/zip")) {
//            try (InputStream inputStream = connection.getInputStream();
//                 FileOutputStream fileOutputStream = new FileOutputStream(outputZipFilePath)) {
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    fileOutputStream.write(buffer, 0, bytesRead);
//                }
//            }
//            return outputZipFilePath; // Retorna la ruta donde se guardó el archivo ZIP
//        }
//
//        // Leer como texto para otros tipos de respuesta
//        BufferedReader reader = new BufferedReader(new InputStreamReader(
//                responseCode >= 200 && responseCode < 300 ? connection.getInputStream() : connection.getErrorStream(),
//                StandardCharsets.UTF_8
//        ));
//
//        String response = reader.lines().collect(Collectors.joining());
//        reader.close();
//        return response; // Retorna la respuesta como String
//    }
//    
//    private String buildRequestBody(String contentType, Map<String, String> formParams, String jsonBody) {
//        if (contentType == null) {
//            return null;
//        }
//
//        switch (contentType) {
//            case "application/json":
//                return jsonBody != null ? jsonBody : "";
//            case "application/x-www-form-urlencoded":
//                return formParams != null ? formParams.entrySet()
//                        .stream()
//                        .map(e -> e.getKey() + "=" + e.getValue())
//                        .collect(Collectors.joining("&"))
//                        : "";
//            case "multipart/form-data":
//                // Aquí podrías implementar lógica para manejar multipart si lo necesitas
//                throw new UnsupportedOperationException("Multipart/form-data aún no está implementado.");
//            default:
//                return null;
//        }
//    }
    
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author user
 */
public class HttpClient {

    /**
     * Método genérico para realizar llamadas HTTP.
     *
     * @param urlString   URL del endpoint.
     * @param method      Método HTTP (GET, POST, PUT, DELETE, etc.).
     * @param headers     Headers adicionales para la solicitud.
     * @param requestBody Cuerpo de la solicitud (puede ser null para métodos como GET o DELETE).
     * @return Respuesta como String.
     * @throws IOException En caso de error de conexión o lectura.
     */
    public static String sendRequest(
            String urlString,
            String method,
            Map<String, String> headers,
            String requestBody) throws IOException {

        // Crear la conexión
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            // Configurar el método HTTP
            connection.setRequestMethod(method.toUpperCase());
            connection.setDoInput(true); // Permitir recibir datos

            // Si el método es POST o PUT, permitir enviar datos
            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                connection.setDoOutput(true);
            }

            // Agregar los headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    connection.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            // Escribir el cuerpo de la solicitud si existe
            if (requestBody != null && !requestBody.isEmpty()) {
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.getBytes("UTF-8"));
                    os.flush();
                }
            }

            // Leer la respuesta
            int responseCode = connection.getResponseCode();
            InputStream inputStream = responseCode < HttpURLConnection.HTTP_BAD_REQUEST
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }

        } finally {
            // Asegurarse de cerrar la conexión
            connection.disconnect();
        }
    }
}

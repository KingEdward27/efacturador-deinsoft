/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util.validacion;

//import com.deinsoft.efacturador3.util.GenericHttpClient;
import com.deinsoft.efacturador3.util.GenericRestClient;
//import com.deinsoft.efacturador3.util.HttpClient;
import com.deinsoft.efacturador3.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
//import com.google.common.base.Splitter;
//import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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

    private String getToken0() throws Exception {
//        GenericHttpClient client = new GenericHttpClient();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("scope", "https://api-sire.sunat.gob.pe");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

//        String requestBody = "grant_type=password"
//                + "&scope=https://api-sire.sunat.gob.pe"
//                + "&client_id=" + clientId
//                + "&client_secret=" + clientSecret
//                + "&username=" + username
//                + "&password=" + password;
//        
        if (Util.isNullOrEmpty(clientId) || Util.isNullOrEmpty(clientSecret)) {
            throw new Exception("No se encontraron credenciales para la conexión con SIRE SUNAT");
        }

        String url = "https://api-seguridad.sunat.gob.pe/v1/clientessol/" + clientId + "/oauth2/token/";
//
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/x-www-form-urlencoded");
        try {
//            HttpResponse<String> response = client.sendRequestFormBody("POST", url, headers, body, HttpResponse.BodyHandlers.ofString());

//            String response = HttpClient.sendRequest(
//                    url,
//                    "POST",
//                    headers,
//                    requestBody
//            );
//            Map<String, Object> response = Util.simpleApiWithFormBody(url, HttpMethod.POST, null, body);
//            Map<String, Object> result
//                    = new ObjectMapper().readValue(response, HashMap.class);
            GenericRestClient client = new GenericRestClient();

            Map<String, Object> response = client.sendRequestFormBody(
                    url,
                    HttpMethod.POST,
                    headers, // Sin headers
                    body, // Sin cuerpo
                    new ParameterizedTypeReference<Map<String, Object>>() {
            }
            );
            token = response.get("access_token").toString();
//            token = result.get("access_token").toString();
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    private String getToken() throws Exception {
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("grant_type", "password");
//        body.add("scope", "https://api-sire.sunat.gob.pe");
//        body.add("client_id", clientId);
//        body.add("client_secret", clientSecret);
//        body.add("username", username);
//        body.add("password", password);
//
//        Map<String, Object> credentialMapResponse = Util.simpleApiWithFormBody(
//                "https://api-seguridad.sunat.gob.pe/v1/clientessol/330c5519-61fc-40a9-af27-986b23220b3e/oauth2/token/",
//                HttpMethod.POST, "", body);
//
//        if (credentialMapResponse == null) {
//            throw new Exception("Respuesta nula en servicio de autenticación");
//        }
//        token = credentialMapResponse.get("access_token").toString();
//        return token;
//    }
    public List<PeriodoResponse> getPeriodos(String codLibro) throws JsonProcessingException, Exception {
        ObjectMapper mapper = new ObjectMapper();
//        GenericHttpClient client = new GenericHttpClient();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Bearer " + token);
//        Map headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);//MediaType.APPLICATION_FORM_URLENCODED);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
//        HttpResponse<String> bodyPeriodos = client.sendRequestJsonBody(//140000
//                HttpMethod.GET.toString(), "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/padron/web/omisos/{{codLibro}}/periodos"
//                .replace("{{codLibro}}", codLibro),
//                headers,
//                "", HttpResponse.BodyHandlers.ofString());

//        String responseBody = Util.simpleApiWithJsonBody2("https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/padron/web/omisos/{{codLibro}}/periodos"
//                            .replace("{{codLibro}}", codLibro), HttpMethod.GET, headers, 
//                            null, null);
//        String responseBody = Util.simpleApiWithJsonBody("https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/padron/web/omisos/{{codLibro}}/periodos"
//                            .replace("{{codLibro}}", codLibro), "", HttpMethod.GET, token, null, null);
        GenericRestClient client = new GenericRestClient();
        String response = client.sendRequest(
                "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/padron/web/omisos/{{codLibro}}/periodos"
                        .replace("{{codLibro}}", codLibro),
                HttpMethod.GET,
                headers,
                null,
                new ParameterizedTypeReference<String>() {
        }
        );

        return mapper.readValue(response, new TypeReference<List<PeriodoResponse>>() {
        });

        //return mapper.readValue(bodyPeriodos.body(), PeriodoRootResponse.class);
    }

    public String getResumen(String periodo, String codTipoResumen, String CodTipoArchivo, String codLibro) throws JsonProcessingException, Exception {
        ObjectMapper mapper = new ObjectMapper();
//        GenericHttpClient client = new GenericHttpClient();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Authorization", "Bearer " + token);
//        HttpResponse<String> bodyPeriodos = client.sendRequestJsonBody(
//                HttpMethod.GET.toString(), "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/resumen/web/resumencomprobantes/"
//                + "{{perTributario}}/{{codTipoResumen}}/{{codTipoArchivo}}/exporta?codLibro={{codLibro}}"
//                        .replace("{{perTributario}}", periodo)
//                        .replace("{{codTipoResumen}}", codTipoResumen)
//                        .replace("{{codTipoArchivo}}", CodTipoArchivo)
//                        .replace("{{codLibro}}", codLibro),
//                headers,
//                "", HttpResponse.BodyHandlers.ofString());

        GenericRestClient client = new GenericRestClient();
        String response = client.sendRequest(
                "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/resumen/web/resumencomprobantes/"
                + "{{perTributario}}/{{codTipoResumen}}/{{codTipoArchivo}}/exporta?codLibro={{codLibro}}"
                        .replace("{{perTributario}}", periodo)
                        .replace("{{codTipoResumen}}", codTipoResumen)
                        .replace("{{codTipoArchivo}}", CodTipoArchivo)
                        .replace("{{codLibro}}", codLibro),
                HttpMethod.GET,
                headers,
                null,
                new ParameterizedTypeReference<String>() {
        }
        );

        return response;
    }

    public Map<String, Object> getPropuestaRce(String periodo, String codTipoArchivo, String codOrigenEnvio) throws JsonProcessingException, Exception {
        ObjectMapper mapper = new ObjectMapper();
//        GenericHttpClient client = new GenericHttpClient();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
//        HttpResponse<String> bodyPeriodos = client.sendRequestJsonBody(
//                HttpMethod.GET.toString(), "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rce/propuesta/web/propuesta/"
//                + "{{perTributario}}/exportacioncomprobantepropuesta?codTipoArchivo={{codTipoArchivo}}&codOrigenEnvio={{codOrigenEnvio}}"
//                        .replace("{{perTributario}}", periodo)
//                        .replace("{{codTipoArchivo}}", codTipoArchivo)
//                        .replace("{{codOrigenEnvio}}", codOrigenEnvio),
//                headers,
//                "", HttpResponse.BodyHandlers.ofString());
//        //response: numTicket
//        return mapper.readValue(bodyPeriodos.body(), new TypeReference<Map<String, Object>>() {
//        });

        GenericRestClient client = new GenericRestClient();
        String response = client.sendRequest(
                "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rce/propuesta/web/propuesta/"
                + "{{perTributario}}/exportacioncomprobantepropuesta?codTipoArchivo={{codTipoArchivo}}&codOrigenEnvio={{codOrigenEnvio}}"
                        .replace("{{perTributario}}", periodo)
                        .replace("{{codTipoArchivo}}", codTipoArchivo)
                        .replace("{{codOrigenEnvio}}", codOrigenEnvio),
                HttpMethod.GET,
                headers,
                null,
                new ParameterizedTypeReference<String>() {
        }
        );

        return mapper.readValue(response, new TypeReference<Map<String, Object>>() {
        });
    }

    public Map<String, Object> getPropuestaRvie(String periodo, String codTipoArchivo) throws JsonProcessingException, Exception {
        ObjectMapper mapper = new ObjectMapper();
//        GenericHttpClient client = new GenericHttpClient();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
//        HttpResponse<String> bodyPeriodos = client.sendRequestJsonBody(
//                HttpMethod.GET.toString(), "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvie/propuesta/web/propuesta"
//                        + "/{{perTributario}}/exportapropuesta?codTipoArchivo={{codTipoArchivo}}"
//                        .replace("{{perTributario}}", periodo)
//                        .replace("{{codTipoArchivo}}", codTipoArchivo),
//                headers,
//                "", HttpResponse.BodyHandlers.ofString());

        GenericRestClient client = new GenericRestClient();
        String response = client.sendRequest(
                "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvie/propuesta/web/propuesta"
                + "/{{perTributario}}/exportapropuesta?codTipoArchivo={{codTipoArchivo}}"
                        .replace("{{perTributario}}", periodo)
                        .replace("{{codTipoArchivo}}", codTipoArchivo),
                HttpMethod.GET,
                headers,
                null,
                new ParameterizedTypeReference<String>() {
        }
        );
        return mapper.readValue(response, new TypeReference<Map<String, Object>>() {
        });
        //return mapper.readValue(bodyPeriodos.body(), PeriodoRootResponse.class);
    }

    public Map<String, Object> getEstadoTicket(String periodo, String numTicket) throws JsonProcessingException, Exception {
        ObjectMapper mapper = new ObjectMapper();
//        GenericHttpClient client = new GenericHttpClient();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
//        HttpResponse<String> bodyPeriodos = client.sendRequestJsonBody(
//                HttpMethod.GET.toString(), "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/gestionprocesosmasivos/web/masivo/consultaestadotickets?"
//                + "perIni={{perTributario}}&perFin={{perTributario}}&page=1&perPage=1&numTicket={{numTicket}}"
//                        .replace("{{perTributario}}", periodo)
//                        .replace("{{numTicket}}", numTicket),
//                headers,
//                "", HttpResponse.BodyHandlers.ofString());

        GenericRestClient client = new GenericRestClient();
        String response = client.sendRequest(
                "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/gestionprocesosmasivos/web/masivo/consultaestadotickets?"
                + "perIni={{perTributario}}&perFin={{perTributario}}&page=1&perPage=1&numTicket={{numTicket}}"
                        .replace("{{perTributario}}", periodo)
                        .replace("{{numTicket}}", numTicket),
                HttpMethod.GET,
                headers,
                null,
                new ParameterizedTypeReference<String>() {
        }
        );
        return mapper.readValue(response, new TypeReference<Map<String, Object>>() {
        });
        //return mapper.readValue(bodyPeriodos.body(), PeriodoRootResponse.class);
    }

    public byte[] getArchivo(String periodo, String nomArchivo, String codTipoArchivo, String codLibro, String numTicket) throws JsonProcessingException, Exception {
        ObjectMapper mapper = new ObjectMapper();
//        GenericHttpClient client = new GenericHttpClient();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        String url = "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/gestionprocesosmasivos/web/masivo/archivoreporte?"
                + "nomArchivoReporte=" + nomArchivo
                + "&codTipoArchivoReporte=" + codTipoArchivo
                + "&codLibro=" + codLibro
                + "&perTributario=" + periodo
                + "&codProceso=10"
                + "&numTicket=" + numTicket;
//                .replace("{{nomArchivoReporte}}", nomArchivo)
//                .replace("{{codTipoArchivo}}", codTipoArchivo)
//                .replace("{{codLibro}}", codLibro)
//                .replace("{{perTributario}}", periodo)
//                .replace("{{numTicket}}", numTicket);

        GenericRestClient client = new GenericRestClient();
        byte[] response = client.sendRequest(url,
                HttpMethod.GET,
                headers,
                null,
                new ParameterizedTypeReference<byte[]>() {
        }
        );

//        HttpResponse<byte[]> bodyFile = client.sendRequestJsonBody(
//                HttpMethod.GET.toString(), url,
//                headers,
//                "", HttpResponse.BodyHandlers.ofByteArray());
        //response: numTicket
        return response;
        //return mapper.readValue(bodyPeriodos.body(), PeriodoRootResponse.class);
    }

//    public Object getArchivo2(String periodo, String nomArchivo, String codTipoArchivo, String codLibro, String numTicket) throws JsonProcessingException, Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        GenericHttpClient client = new GenericHttpClient();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Bearer " + token);
//        headers.put("Accept", "application/zip");
//        String outputZipFilePath = "output.zip";
//        String url = "https://api-sire.sunat.gob.pe/v1/contribuyente/migeigv/libros/rvierce/gestionprocesosmasivos/web/masivo/archivoreporte?"
//                + "nomArchivoReporte=" + nomArchivo
//                + "&codTipoArchivoReporte=" + codTipoArchivo
//                + "&codLibro=" + codLibro
//                + "&perTributario=" + periodo
//                + "&codProceso=10"
//                + "&numTicket=" + numTicket;
////                .replace("{{nomArchivoReporte}}", nomArchivo)
////                .replace("{{codTipoArchivo}}", codTipoArchivo)
////                .replace("{{codLibro}}", codLibro)
////                .replace("{{perTributario}}", periodo)
////                .replace("{{numTicket}}", numTicket);
//
//        Object response = client.sendRequest(
//                HttpMethod.GET.toString(), url,
//                headers,
//                "application/zip", null, null, outputZipFilePath);
//
//        if (response instanceof String) {
//            System.out.println("Response as String: " + response);
//        } else if (response instanceof String) { // Path to ZIP file
//            System.out.println("ZIP file saved at: " + response);
//        }
//
//        //response: numTicket
//        return response;
//        //return mapper.readValue(bodyPeriodos.body(), PeriodoRootResponse.class);
//    }
}

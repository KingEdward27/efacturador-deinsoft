/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.deinsoft.efacturador3.util;

import com.google.common.io.Files;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import org.apache.commons.io.IOUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
//import net.sf.sevenzipjbinding.*;
//import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
//import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
//import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

/**
 *
 * @author EDWARD-PC
 */
public class Util {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static BigDecimal getBigDecimalValue(Map<String, Object> map, String key) {
        if (map.get(key) == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(map.get(key).toString());
    }

    public static String getStringValue(Map<String, Object> map, String key) {
        if (map.get(key) == null) {
            return null;
        }
        return map.get(key).toString();
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[in.available()];
        int len;
        // read bytes from the input stream and store them in buffer
        while ((len = in.read(buffer)) != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    public static boolean isNullOrEmpty(Object o) {
        try {
            if (o == null) {
                return true;
            }
            if (String.valueOf(o).equals("")) {
                return true;
            }
            return false;
        } catch (NullPointerException e) {
            return true;
        }

    }

    public static Map<String, Object> toMap(Object object, String[] visibles) {
        Map<String, Object> map = new HashMap<>();
        try {

            for (Field f : object.getClass().getDeclaredFields()) {
//            System.out.println(f.toString());
//                System.out.println(f.getGenericType() + " " + f.getName() + " " + f.getType());
                System.out.println(f.getGenericType() + " " + f.getName() + " " + f.getModifiers());
                if (visibles == null) {
                    if (f.toString().contains("final") || f.toString().contains("list")) {
                        continue;
                    }
                } else {
                    if (f.toString().contains("final") || f.toString().contains("list")
                            || !Arrays.asList(visibles).contains(f.getName())) {
                        continue;
                    }
                }

                map.put(f.getName(), f.get(object));

//                objectBuilder.add(f.getName(), f.get(facturaElectronica).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static byte[] OutputStreamToByteArray(OutputStream myOutputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.writeTo(myOutputStream);
        byte[] x = baos.toByteArray();
        return x;
    }

    public Map<String, Object> simpleApi(HttpMethod httpMethod, String url, String token, Map<String, String> params) throws Exception {
        Map<String, Object> respuesta = null;
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", token);
        headers.add("Content-Type", "application/json");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            multiValueMap.put(entry.getKey(), Arrays.asList(entry.getValue()));
        }

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
                //                .queryParam("numero", "{numero}")
                .queryParams(multiValueMap)
                .encode()
                .toUriString();

        HttpEntity<MultiValueMap<String, String>> entityReq = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = getRestTemplate().exchange(urlTemplate,
                httpMethod, entityReq,
                Map.class, params);

        if (response.hasBody() && (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED)) {
            respuesta = response.getBody();
            if (respuesta != null) {
                return respuesta;
            } else {
                String msg = "Respuesta vacía del API";
                throw new Exception(msg);
            }
        } else {
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

    public static byte[] comprimirArchivo(InputStream entrada, String nombre) throws Exception {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        ZipEntry ze = new ZipEntry(nombre);
        zos.putNextEntry(ze);

        int len;
        while ((len = entrada.read(buffer)) > 0) {
            zos.write(buffer, 0, len);
        }

        entrada.close();
        zos.closeEntry();

        zos.close();
        return bos.toByteArray();
    }

    public static byte[] generateFile(String fileName, String content) {
        try {
            File tempDir = Files.createTempDir();
            File file = Paths.get(tempDir.getAbsolutePath(), fileName).toFile();
            try ( FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.append(content).flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return toByteArray(new FileInputStream(file));
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public static boolean validaCorreo(String email) {
        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    public static String encryptMessage(byte[] message, String encKeyString) throws InvalidKeyException, NoSuchPaddingException,
            NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] decodedKey = Base64.getDecoder().decode(encKeyString);
//        SecretKey secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
        SecretKey secretKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(message);
        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    public static String decryptMessage(byte[] encryptedMessage, String encKeyString) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] decodedKey = Base64.getDecoder().decode(encKeyString);
//        SecretKey secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
        SecretKey secretKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] clearMessage = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(clearMessage);
    }

    public static String readFile(InputStream is, Charset encoding) throws IOException {
//        byte[] encoded = Files.readAllBytes(Paths.get(path));
        byte[] encoded = IOUtils.toByteArray(is);
        return new String(encoded, encoding);
    }

    public static String simpleApiWithJsonBody(String urlParam, String jsonBody, HttpMethod httpMethod, String authorization,
            String accept,
            String contentType) {
        boolean result = false;
        System.out.println("jsonBody: " + jsonBody);
        try {
            URL url = new URL(urlParam);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod(httpMethod.name());
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Authorization", "Bearer " + authorization);
//            conn.setRequestProperty("Content-Type", contentType);
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
            return jsonString;
        } catch (ConnectException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String simpleApiWithJsonBody2(String urlParam, HttpMethod httpMethod, 
            HttpHeaders headers,
            String body,
            String accept) throws Exception {
        Map<String, Object> respuesta = null;
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(contentType);//MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("Authorization", "bearer " + authorization);
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(urlParam)
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
//        body.add("grant_type", "client_credentials");
//        body.add("scope", "https://api.sunat.gob.pe/v1/contribuyente/contribuyentes");
//        body.add("client_id", client_id);
//        body.add("client_secret", client_secret);

        HttpEntity<String> entityReq = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = getRestTemplate().exchange(urlTemplate,
                httpMethod, entityReq,
                Map.class, params);

        if (response.hasBody() && (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED)) {
            respuesta = response.getBody();
            if (respuesta != null) {
                return respuesta.toString();
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

    public static Map<String, Object> simpleApiWithFormBody(String urlParam, HttpMethod httpMethod, String authorization, 
            MultiValueMap<String, String> formBody) throws Exception {
        Map<String, Object> respuesta = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "bearer " + authorization);
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(urlParam)
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
//        body.add("grant_type", "client_credentials");
//        body.add("scope", "https://api.sunat.gob.pe/v1/contribuyente/contribuyentes");
//        body.add("client_id", client_id);
//        body.add("client_secret", client_secret);

        HttpEntity<MultiValueMap<String, String>> entityReq = new HttpEntity<>(formBody, headers);
        
        ResponseEntity<Map> response = getRestTemplate().exchange(urlTemplate,
                httpMethod, entityReq,
                Map.class, params);

        if (response.hasBody() && (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED)) {
            respuesta = response.getBody();
            if (respuesta != null) {
                return respuesta;
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
//    public static List<Object[]> importExcel(MultipartFile reapExcelDataFile, boolean includeFirstLine) throws IOException {
//
//        List<Object[]> listResult = null;
//        try {
//            Workbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
//            Sheet datatypeSheet = workbook.getSheetAt(0);
//            int columnsCount = datatypeSheet.getRow(0).getLastCellNum();
//            listResult = new ArrayList<>();
//            Iterator<Row> iterator = datatypeSheet.iterator();
//            int row = -1;
//            int column = 0;
//            while (iterator.hasNext()) {
//                Object[] rowArray = new Object[columnsCount];
//                Row currentRow = iterator.next();
//                Iterator<Cell> cellIterator = currentRow.iterator();
//                column = 0;
//                row++;
//                Cell currentCell;
//                for (int i = 0; i < currentRow.getLastCellNum(); i++) {
//                    currentCell = currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                    if (null == currentCell.getCellTypeEnum()) {
//                        column++;
//                        break;
//                    } else //getCellTypeEnum shown as deprecated for version 3.15
//                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
//                    {
//                        switch (currentCell.getCellType()) {
//                            case STRING:
//                                rowArray[column] = currentCell.getStringCellValue();
//                                break;
//                            case NUMERIC:
////                                rowArray[column] = currentCell.getNumericCellValue();
//                                if (DateUtil.isCellDateFormatted(currentCell)) {
//                                    rowArray[column] = currentCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                                } else {
//                                    DataFormatter fmt = new DataFormatter();
//
//
//                                    rowArray[column] = fmt.formatCellValue(currentCell);
//                                    //rowArray[column] = currentCell.getNumericCellValue();
//                                    
//                                }
//                                break;
//                            default:
//                                rowArray[column] = currentCell.getStringCellValue();
//                                break;
//                        }
//                    }
//
//                    column++;
//                }
//                if (includeFirstLine && row == 0) {
//                    listResult.add(rowArray);
//                } else if (row > 0) {
//                    listResult.add(rowArray);
//                }
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return listResult;
//    }

    public static LocalDate getDateFromString(String fechaVencimiento, DateTimeFormatter format) {
        try {
            if (fechaVencimiento.equals("")) {
                return null;
            }
            format = format.withLocale(Locale.getDefault());  // Locale specifies human language for translating, and cultural norms for lowercase/uppercase and abbreviations and such. Example: Locale.US or Locale.CANADA_FRENCH
            LocalDate date = LocalDate.parse(fechaVencimiento, format);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getStringValue(Object o) {
        if (o == null) {
            return null;
        }
        if (o != null) {
            return String.valueOf(o).trim();
        }
        return null;
    }

    public static boolean validateFormatDate(String inputTimeString) {
        try {
            if (isNullOrEmpty(inputTimeString)) {
                return false;
            }
            LocalDate.parse(inputTimeString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String generateSafeToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[36]; // 36 bytes * 8 = 288 bits, a little bit more than
        // the 256 required bits 
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(bytes);
    }

    public static void decompressZip(InputStream zipFileInputStream, String outputDir) throws IOException {
        File outputDirectory = new File(outputDir);

        // Crear el directorio de salida si no existe
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        try (ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(zipFileInputStream, "ISO-8859-1", true, true, true)) {
            ZipArchiveEntry entry;

            // Leer cada entrada del ZIP
            while ((entry = zipInputStream.getNextZipEntry()) != null) {
                File outputFile = new File(outputDirectory, entry.getName());

                if (entry.isDirectory()) {
                    // Crear directorios si es necesario
                    outputFile.mkdirs();
                } else {
                    // Asegurarse de que los directorios padres existan
                    outputFile.getParentFile().mkdirs();

                    // Escribir el archivo descomprimido
                    try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        }
    }
    
    public static void descomprimirZip(String ficheroZip, String directorioSalida)
            throws Exception {
        final int TAM_BUFFER = 4096;
        byte[] buffer = new byte[TAM_BUFFER];

        ZipInputStream flujo = null;
        
        // Crear el directorio de destino si no existe
        File dir = new File(directorioSalida);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        try {
            flujo = new ZipInputStream(new BufferedInputStream(
                    new FileInputStream(ficheroZip)));
            ZipEntry entrada;
            while ((entrada = flujo.getNextEntry()) != null) {
                String nombreSalida = directorioSalida + File.separator
                        + entrada.getName();
                if (entrada.isDirectory()) {
                    File directorio = new File(nombreSalida);
                    directorio.mkdir();
                } else {
                    BufferedOutputStream salida = null;
                    try {
                        int leido;
                        salida = new BufferedOutputStream(
                                new FileOutputStream(nombreSalida), TAM_BUFFER);
                        while ((leido = flujo.read(buffer, 0, TAM_BUFFER)) != -1) {
                            salida.write(buffer, 0, leido);
                        }
                    } finally {
                        if (salida != null) {
                            salida.close();
                        }
                    }
                }
            }
        } finally {
            if (flujo != null) {
                flujo.close();
            }

        }
    }


    private static void extractFile(ZipInputStream zis, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
    
    public static <T> List<T> parseAndMap(List<String> inputList, String separator, Function<String[], T> mapper) {
        List<T> resultList = new ArrayList<>();

        for (String input : inputList) {
            if (input != null && !input.isEmpty()) {
                // Divide la cadena en un array usando el separador
                String[] parts = input.split(separator);

                // Usa el mapper para convertir el array en un objeto genérico
                T mappedObject = mapper.apply(parts);

                // Añade el objeto mapeado a la lista de resultados
                resultList.add(mappedObject);
            }
        }

        return resultList;
    }

    public static <A, B, C> Stream<C> zip(Stream<A> streamA, Stream<B> streamB, BiFunction<A, B, C> zipper) {
        final Iterator<A> iteratorA = streamA.iterator();
        final Iterator<B> iteratorB = streamB.iterator();
        final Iterator<C> iteratorC = new Iterator<C>() {
            @Override
            public boolean hasNext() {
                return iteratorA.hasNext();// && iteratorB.hasNext();
            }

            @Override
            public C next() {
                return zipper.apply(iteratorA.next(), iteratorB == null? null: iteratorB.next());
            }
        };
        final boolean parallel = streamA.isParallel() || streamB.isParallel();
        return iteratorToFiniteStream(iteratorC, parallel);
    }

    public static <T> Stream<T> iteratorToFiniteStream(Iterator<T> iterator, boolean parallel) {
        final Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }
    
    public static byte[] createZipInMemory(String content, String fileName) throws IOException {
        // Crear un ByteArrayOutputStream para almacenar el ZIP en memoria
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Usar un ZipOutputStream para escribir el archivo ZIP
        try ( ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            // Crear una entrada ZIP con el nombre del archivo
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOutputStream.putNextEntry(zipEntry);

            // Escribir el contenido del archivo en la entrada ZIP
            zipOutputStream.write(content.getBytes());

            // Cerrar la entrada ZIP
            zipOutputStream.closeEntry();
        }

        // Retornar los bytes del ZIP generado
        return byteArrayOutputStream.toByteArray();
    }
    
//    public static void decompressSplitZip(String splitZipFilePath, String outputDir) throws Exception {
//        File file = new File(splitZipFilePath);
//        if (!file.exists()) {
//            throw new RuntimeException("Archivo no encontrado: " + splitZipFilePath);
//        }
//
//        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
//
//        try (ISevenZipInArchive inArchive = SevenZip.openInArchive(
//                SevenZipArchiveFormat.ZIP, new RandomAccessFileInStream(randomAccessFile))) {
//
//            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
//
//            for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
//                if (item.isFolder()) {
//                    continue; // Crear carpetas después si es necesario
//                }
//
//                File outFile = new File(outputDir, item.getPath());
//                outFile.getParentFile().mkdirs();
//
//                try (FileOutputStream fos = new FileOutputStream(outFile)) {
//                    ExtractOperationResult result = item.extractSlow(data -> {
//                        fos.write(data);
//                        return data.length;
//                    });
//
//                    if (result != ExtractOperationResult.OK) {
//                        System.err.println("Error extrayendo archivo: " + item.getPath());
//                    }
//                }
//            }
//        }
//    }
    
}

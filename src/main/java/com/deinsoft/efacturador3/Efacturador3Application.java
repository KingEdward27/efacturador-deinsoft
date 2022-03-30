package com.deinsoft.efacturador3;

import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class Efacturador3Application {

    @Autowired
    FacturaElectronicaService facturaElectronicaService;
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Efacturador3Application.class, args);
    }

    @Scheduled(fixedDelay = 2000)
    void sendSunat() {
        facturaElectronicaService.sendToSUNAT();
    }
//    public static String Encriptar(String texto) {
//        try {
//            String secretKey = "qualityinfosolutions";
//            String base64EncryptedString = "";
//
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
//            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
//
//            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
//            Cipher cipher = Cipher.getInstance("DESede");
//            cipher.init(1, key);
//
//            byte[] plainTextBytes = texto.getBytes("utf-8");
//            byte[] buf = cipher.doFinal(plainTextBytes);
//            byte[] base64Bytes = Base64.encodeBase64(buf);
//            base64EncryptedString = new String(base64Bytes);
//
//            return base64EncryptedString;
//        } catch (Exception e) {
//            throw new RuntimeException("Error al des encriptar", e);
//        }
//    }

}

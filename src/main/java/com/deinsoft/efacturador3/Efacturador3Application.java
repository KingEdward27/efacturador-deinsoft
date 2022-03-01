package com.deinsoft.efacturador3;

import com.deinsoft.efacturador3.config.XsltCpePath;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import java.io.*;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.ElementProxy;
import org.w3c.dom.Document;

@SpringBootApplication
//@EnableConfigurationProperties(XsltCpePath.class)
public class Efacturador3Application {

    private static final String PRIVATE_KEY_ALIAS = "certContribuyente";
    private static final String PRIVATE_KEY_PASS = "123456";
    private static final String KEY_STORE_PASS = "SuN@TF4CT";
    private static final String KEY_STORE_TYPE = "JKS";

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Efacturador3Application.class, args);
        System.out.println(Encriptar("123456"));

        final InputStream fileInputStream = new FileInputStream("C:/ServicioFacturacion/XML/20518964756-03-B001-00009614.xml");
        try {
            output(signFile(fileInputStream, new File("D:/DEFACT/ALMCERT/FacturadorKey.jks")), "D:/DEFACT/20518964756/TEMP/signed-test2.xml");
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    public static String Encriptar(String texto) {
        try {
            String secretKey = "qualityinfosolutions";
            String base64EncryptedString = "";

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(1, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

            return base64EncryptedString;
        } catch (Exception e) {
            throw new RuntimeException("Error al des encriptar", e);
        }
    }

    public static ByteArrayOutputStream signFile(InputStream xmlFile, File privateKeyFile) throws Exception {
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        Init.init();
        ElementProxy.setDefaultPrefix(Constants.SignatureSpecNS, "");
        final KeyStore keyStore = loadKeyStore(privateKeyFile);
        final XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_RSA);
        final Transforms transforms = new Transforms(doc);
        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
        final Key privateKey = keyStore.getKey(PRIVATE_KEY_ALIAS, PRIVATE_KEY_PASS.toCharArray());
        final X509Certificate cert = (X509Certificate) keyStore.getCertificate(PRIVATE_KEY_ALIAS);
        sig.addKeyInfo(cert);
        sig.addKeyInfo(cert.getPublicKey());
        sig.sign(privateKey);
        doc.getDocumentElement().appendChild(sig.getElement());
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS).canonicalizeSubtree(doc));
        return outputStream;
    }

    private static KeyStore loadKeyStore(File privateKeyFile) throws Exception {
        final InputStream fileInputStream = new FileInputStream(privateKeyFile);
        try {
            final KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(fileInputStream, KEY_STORE_PASS.toCharArray());
            return keyStore;
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

    private static void output(ByteArrayOutputStream signedOutputStream, String fileName) throws IOException {
        final OutputStream fileOutputStream = new FileOutputStream(fileName);
        try {
            fileOutputStream.write(signedOutputStream.toByteArray());
            fileOutputStream.flush();
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }
//        @Bean
//        ApplicationRunner applicationRunner(XsltCpePath xsltCpePath){
//            return args ->{
//            System.out.println(xsltCpePath);
//            };
//        }
}

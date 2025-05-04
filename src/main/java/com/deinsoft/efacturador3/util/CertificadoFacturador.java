package com.deinsoft.efacturador3.util;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

public class CertificadoFacturador {

    private static final Logger log = LoggerFactory.getLogger(CertificadoFacturador.class);

    public String validaCertificado(String input, String numRuc, String passPrivateKey) throws Exception {
//        FileInputStream fis;
        log.debug("GenerarDocumentosServiceImpl.validaCertificado...Iniciando validacion de certificado");
        String validacion = "", alias = "", cadena = "";
        String cadenaValidar = numRuc;
        Boolean certificadoCorrecto = Boolean.valueOf(false);
        Certificate cf = null;

//        try {
//            fis = new FileInputStream(input);
//        } catch (FileNotFoundException e1) {
//            throw new IllegalArgumentException("No se pudo encontrar el archivo: " + input, e1);
//        }

        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");

//            Security.addProvider(new BouncyCastleProvider());
//            KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
            try (InputStream fis = new FileInputStream(input)) {
                ks.load(fis, passPrivateKey.toCharArray());
            }

//            ks.load(fis, passPrivateKey.toCharArray());

            for (Enumeration<String> e = ks.aliases(); e.hasMoreElements()
                    && !certificadoCorrecto.booleanValue();) {
                alias = e.nextElement();
                log.debug("GenerarDocumentosServiceImpl.validaCertificado...Alias: " + alias);
                if (ks.isKeyEntry(alias)) {
                    cf = ks.getCertificate(alias);
                    log.debug("GenerarDocumentosServiceImpl.validaCertificado...cf.toString: " + cf.toString());
                    cadena = cf.toString();

                    if (cadena.indexOf(cadenaValidar) > 0) {
                        certificadoCorrecto = Boolean.valueOf(true);
                    }
                }

            }

        } catch (KeyStoreException e1) {
            throw new Exception("No se pudo procesar el certificado: " + e1.getMessage(), e1);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("No se pudo procesar el certificado: " + e.getMessage(), e);
        } catch (CertificateException e) {
            throw new Exception("No se pudo procesar el certificado: " + e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("No se pudo procesar el certificado: " + e.getMessage(), e);
        }

        if (!certificadoCorrecto.booleanValue()) {
            validacion = "El propietario del certificado no es el RUC " + numRuc;
        } else {
            validacion = "[ALIAS]:" + alias;
        }
        log.debug("GenerarDocumentosServiceImpl.validaCertificado...Finalizando validacion de certificado");
        return validacion;
    }

    public HashMap<String, Object> importarCertificado(HashMap<String, Object> obj) throws Exception {
        String aliasPfx = "";
        Integer error = Integer.valueOf(0);
        HashMap<String, Object> resultado = new HashMap<>();
        String nombreCertificado = (obj.get("nombreCertificado") != null) ? (String) obj.get("nombreCertificado") : "";
        String passPrivateKey = (obj.get("passPrivateKey") != null) ? (String) obj.get("passPrivateKey") : "";
        String numDoc = (obj.get("numDoc") != null) ? (String) obj.get("numDoc") : "";
        String rootPath = (obj.get("rootPath") != null) ? (String) obj.get("rootPath") : "";
        String companyPath = rootPath + numDoc;
        String rutaCertificado = companyPath + "/" + Constantes.CONSTANTE_CERT + "/" + nombreCertificado;
        String certGenericPath = rootPath + "ALMCERT/";
        log.info(rutaCertificado);
        log.info(certGenericPath + "FacturadorKey.jks");
        resultado.put("validacion", "EXITO");
        if ("".equals(rutaCertificado)) {
            resultado.put("validacion", "Debe ingresar la ruta del certificado");
            error = Integer.valueOf(1);
        }

        if (rutaCertificado.indexOf(".pfx") == -1 && rutaCertificado.indexOf(".p12") == -1 && error.intValue() == 0) {
            resultado.put("validacion", "Archivo cargado debe ser de tipo \"pfx o p12\" ");
            error = Integer.valueOf(1);
        }

        if ("".equals(passPrivateKey) && error.intValue() == 0) {
            resultado.put("validacion", "Debe ingresar su contraseña de certificado");
            error = Integer.valueOf(1);
        }

        if (error.intValue() == 0) {

            try {
                String output = this.validaCertificado(rutaCertificado, numDoc, passPrivateKey);
                log.info("Metodo importarCertificado: Salida de generarDocumentosService.validaCertificado " + output);
                if (!output.contains("[ALIAS]")) {
                    resultado.put("validacion", "Certificado, no esta configurado con el valor del RUC");
                    error = Integer.valueOf(1);
                } else {
                    Integer position = Integer.valueOf(output.indexOf(":") + 1);
                    aliasPfx = output.substring(position.intValue());
                }
            } catch (Exception e) {
                log.info("Mensaje de Error: " + e.getMessage());
                throw e;
            }
        }

        if (error.intValue() == 0) {

//            importPfxToJks(rutaCertificado, passPrivateKey, certGenericPath + "FacturadorKey.jks", "SuN@TF4CT", Constantes.PRIVATE_KEY_ALIAS + numDoc);
            String salida = FacturadorUtil.executeCommand("keytool -delete -alias " + Constantes.PRIVATE_KEY_ALIAS + numDoc + " -storepass SuN@TF4CT -keystore " + certGenericPath + "FacturadorKey.jks");

            log.debug("Metodo importarCertificado: Salida de keytool -delete " + salida);
            String command;
            if (aliasPfx.contains(" ")) {
                command = "keytool -importkeystore -srcalias \"" + aliasPfx + "\" -srckeystore " + rutaCertificado + " -srcstoretype pkcs12 -srcstorepass " + passPrivateKey + " -destkeystore " + certGenericPath + "FacturadorKey.jks -deststoretype JKS -destalias " + Constantes.PRIVATE_KEY_ALIAS + numDoc + " -deststorepass SuN@TF4CT";
            } else {
                command = "keytool -importkeystore -srcalias " + aliasPfx + " -srckeystore " + rutaCertificado + " -srcstoretype pkcs12 -srcstorepass " + passPrivateKey + " -destkeystore " + certGenericPath + "FacturadorKey.jks -deststoretype JKS -destalias " + Constantes.PRIVATE_KEY_ALIAS + numDoc + " -deststorepass SuN@TF4CT";
            }

            log.debug("command: " + command);
            salida = FacturadorUtil.executeCommand(command);

            log.debug("Metodo importarCertificado: Salida de keytool -importkeystore " + salida);
            if (!"".equals(salida)) {
                resultado.put("validacion", "Hubo un error, el certificado no fue creado");
                error = Integer.valueOf(1);
            }



        }
        return resultado;
    }

    private void importPfxToJks(String pfxPath, String pfxPassword,
                                      String jksPath, String jksPassword,
                                      String newAlias) throws Exception {

        // 1. Cargar el JKS existente
        KeyStore jksStore = KeyStore.getInstance("JKS");
        try (FileInputStream jksIn = new FileInputStream(jksPath)) {
            jksStore.load(jksIn, jksPassword.toCharArray());
        }

        // 2. Cargar el archivo PFX

        Security.addProvider(new BouncyCastleProvider());
        KeyStore pfxStore = KeyStore.getInstance("PKCS12", "BC");
        try (FileInputStream pfxIn = new FileInputStream(pfxPath)) {
            pfxStore.load(pfxIn, pfxPassword.toCharArray());
        }

        // 3. Importar la clave y certificado del PFX al JKS
        Enumeration<String> aliases = pfxStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (pfxStore.isKeyEntry(alias)) {
                KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry)
                        pfxStore.getEntry(alias, new KeyStore.PasswordProtection(pfxPassword.toCharArray()));

                Certificate[] chain = pfxStore.getCertificateChain(alias);

                // Añadir al JKS con el alias deseado
                jksStore.setKeyEntry(newAlias, entry.getPrivateKey(), jksPassword.toCharArray(), chain);
                log.info("Importado al JKS con alias: " + newAlias);
            }
        }

        // 4. Guardar cambios en el JKS
        try (FileOutputStream jksOut = new FileOutputStream(jksPath)) {
            jksStore.store(jksOut, jksPassword.toCharArray());
            log.info("JKS actualizado correctamente.");
        }
    }
}

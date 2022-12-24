package com.deinsoft.efacturador3;

import com.deinsoft.efacturador3.model.ResumenDiario;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.ResumenDiarioService;
import com.deinsoft.efacturador3.soap.gencdp.TransferirArchivoException;
import io.github.project.openubl.xmlsenderws.webservices.managers.BillServiceManager;
import io.github.project.openubl.xmlsenderws.webservices.providers.BillServiceModel;
import io.github.project.openubl.xmlsenderws.webservices.wrappers.ServiceConfig;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class Efacturador3Application implements CommandLineRunner {

    @Autowired
    FacturaElectronicaService facturaElectronicaService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ResumenDiarioService resumenDiarioService;
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Efacturador3Application.class, args);
//        ServiceConfig config = new ServiceConfig.Builder()
//            .url("https://e-factura.sunat.gob.pe/ol-ti-itcpfegem/billService")
//            .username("10414316595EBOLETAS") 
//            .password("Eboletas123") 
//            .build();
////            
//        BillServiceModel result =   BillServiceManager.getStatus("202209568409112", config);//202208980058201, 202208984136401
//        System.out.println("result "+result.getStatus());
//        System.out.println("result "+result.getDescription());
//        System.out.println("result "+result.getCode());
//        System.out.println("result "+result.toString());
        String myByteArray = "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";
//        FileUtils.w(
//                    new File("D:/asdad.zip"),
//                    myByteArray.getBytes(Charset.defaultCharset())); 
//        InputStream is = new ByteArrayInputStream(myByteArray.toByteArray());
//        StreamedContent zipedFile= new DefaultStreamedContent(
//                is,   "application/zip","archixxx.zip", Charsets.UTF_8.name());
//        try (FileOutputStream fos = new FileOutputStream("D:/ARCHIVO2.zip")) {
//            fos.write(myByteArray. getBytes());
//            fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
//         }
//        byte[] archivoByte = java.util.Base64.getDecoder().decode(myByteArray);
        // Escribimos el archivo de imagen en el escritorio, si es pdf solo cambia la extensión y el nombre según lo que necesites
//        OutputStream out = new FileOutputStream("D:/202209705854288.zip");
//        out.write(archivoByte);
//        out.close();
    }

//    @Scheduled(cron = "0 0 04 * * *")
//    @Scheduled(cron = "* */5 * * * *")
    void sendSunat() {
        facturaElectronicaService.sendToSUNAT();
//        try {
//            resumenDiarioService.sendSUNAT();
//        } catch (TransferirArchivoException ex) {
//            Logger.getLogger(Efacturador3Application.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void run(String... args) throws Exception {
        String password = "DEINSOFT202201$$";

        for (int i = 0; i < 2; i++) {
            String bcryptPassword = passwordEncoder.encode(password);
            System.out.println(bcryptPassword);
        }
    }
}

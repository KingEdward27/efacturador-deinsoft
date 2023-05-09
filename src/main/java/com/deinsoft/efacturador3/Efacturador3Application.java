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
//        BillServiceModel result =   BillServiceManager.getStatus("400000036155741", config);//202208980058201, 202208984136401
//        System.out.println("result "+result.getStatus());
//        System.out.println("result "+result.getDescription());
//        System.out.println("result "+result.getCode());
//        System.out.println("result "+result.toString());
        
    }
    
    @Scheduled(cron = "0 0 4 * * *")
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

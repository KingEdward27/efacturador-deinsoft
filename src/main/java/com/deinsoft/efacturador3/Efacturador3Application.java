package com.deinsoft.efacturador3;

import com.deinsoft.efacturador3.model.ResumenDiario;
import com.deinsoft.efacturador3.service.FacturaElectronicaService;
import com.deinsoft.efacturador3.service.ResumenDiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.deinsoft.efacturador3.config.AppConfig;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class Efacturador3Application extends WebMvcConfigurerAdapter implements CommandLineRunner {

    @Autowired
    FacturaElectronicaService facturaElectronicaService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ResumenDiarioService resumenDiarioService;
    
    @Autowired
    AppConfig appConfig;
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Efacturador3Application.class, args);
        
//        ServiceConfig config = new ServiceConfig.Builder()
//            .url("https://e-factura.sunat.gob.pe/ol-ti-itcpfegem/billService")
//            .username("10414316595EBOLETAS") 
//            .password("Eboletas123") 
//            .build();
//////        
//        BillServiceModel result =   BillServiceManager.getStatus("500000113137499", config);//202208980058201, 202208984136401
//        System.out.println("result "+result.getStatus());
//        System.out.println("result "+result.getDescription());
//        System.out.println("result "+result.getCode());
//        if (result.getCdr() != null) {
//            FileUtils.writeByteArrayToFile(
//                    new File("C:/Users/user/DocumentsRPTA/500000113137499.zip"),
//                    result.getCdr());
//        }
        
    }
    
    @Scheduled(cron = "0 0 04 * * *")
    void sendSunat() {
        System.out.println("init sendSunat()");
        facturaElectronicaService.sendToSUNAT();
//        try {
//            resumenDiarioService.sendSUNAT();
//        } catch (TransferirArchivoException ex) {
//            Logger.getLogger(Efacturador3Application.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    @Scheduled(cron = "0 0 03 * * *")
    void verifyPending() {
        System.out.println("init verifyPending()");
        facturaElectronicaService.verifyPending();
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

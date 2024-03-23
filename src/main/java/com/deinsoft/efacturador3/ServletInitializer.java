package com.deinsoft.efacturador3;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        //System.setProperty("server.servlet.context-path", "/deinsoft-facturador");
        return application.sources(Efacturador3Application.class);
    }

}

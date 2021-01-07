package com.alliance.radish;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
public class SpringbootAdminApplication {

    public static void main(String[] args) {
        try{
            SpringApplication.run(SpringbootAdminApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

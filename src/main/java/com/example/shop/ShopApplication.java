package com.example.shop;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication extends VaadinWebSecurity {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}

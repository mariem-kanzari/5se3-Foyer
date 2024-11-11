package com.example.projetdevops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class ProjetDevopsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetDevopsApplication.class, args);
    }

}

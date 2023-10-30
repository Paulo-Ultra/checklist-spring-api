package com.learning.springboot.checklistapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChecklistApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChecklistApiApplication.class, args);
    }

    @Bean
    public OpenAPI custgomOpenApi(){

        return new OpenAPI()
                .info(new Info()
                        .title("Chesklist API for Udemy Course")
                        .description("Sample API create for learning purposes")
                        .contact(new Contact()
                                .name("Paulo Ricardo Freire Ultra")
                                .email("prfultra@yahoo.com.br")
                        )
                        .version("V1")
                        .termsOfService("http://mywebsite.com.br")
                        .license(
                                new License()
                                        .name("MIT")
                        )
                );
    }
}

package com.estoque.sistemacontroleestoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.estoque.sistemacontroleestoque")
public class SistemaControleEstoqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaControleEstoqueApplication.class, args);
    }
}

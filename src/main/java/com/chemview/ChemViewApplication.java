package com.chemview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.chemview")
public class ChemViewApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChemViewApplication.class, args);
    }

}

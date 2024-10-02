package br.com.akrasia.akimob;

import org.springframework.boot.SpringApplication;

public class AkimobApplicationTest {

    public static void main(String[] args) {
        SpringApplication.from(AkimobApplication::main)
                .with(IntegrationTestsConfig.class)
                .run(args);
    }

}

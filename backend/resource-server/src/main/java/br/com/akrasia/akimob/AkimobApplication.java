package br.com.akrasia.akimob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:commons.properties")
public class AkimobApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkimobApplication.class, args);
	}

}

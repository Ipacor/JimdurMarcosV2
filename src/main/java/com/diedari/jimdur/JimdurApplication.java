package com.diedari.jimdur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JimdurApplication {

	public static void main(String[] args) {
		SpringApplication.run(JimdurApplication.class, args);
	}

}

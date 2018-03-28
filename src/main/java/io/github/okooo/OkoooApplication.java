package io.github.okooo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OkoooApplication {

    public static void main(String[] args) {
        SpringApplication.run(OkoooApplication.class, args);
    }

}
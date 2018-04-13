package com.strive.rich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

//@Configuration
//@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class Application {
    public static void main(String args[]) {
        SpringApplication.run(Application.class);
    }
}

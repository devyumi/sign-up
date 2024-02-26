package com.example.signup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SignUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignUpApplication.class, args);
    }

}

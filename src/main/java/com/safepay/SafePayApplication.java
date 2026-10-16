package com.safepay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starts the embedded web server so a transaction can be submitted over HTTP.
 * The CSV import from Lesson 1 still starts from Main.
 */
@SpringBootApplication
public class SafePayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafePayApplication.class, args);
    }
}

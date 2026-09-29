package com.janseva.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {

        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    "D:\\JanSevaAI\\ai-server\\ai_server.py"
            );

            pb.inheritIO();
            pb.start();

            System.out.println("AI Server Started");

        } catch (Exception e) {

            System.out.println(
                    "AI Server Failed : "
                    + e.getMessage()
            );
        }

        SpringApplication.run(
                BackendApplication.class,
                args
        );
    }
}
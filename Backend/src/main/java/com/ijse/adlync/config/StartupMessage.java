package com.ijse.adlync.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupMessage implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║                    SPRING BOOT APPLICATION                   ║\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║                                                              ║\n" +
            "║  🚀 Application is running on: http://localhost:8080        ║\n" +
            "║  📚 Swagger UI: http://localhost:8080/swagger-ui.html       ║\n" +
            "║  📖 API Docs: http://localhost:8080/api-docs                ║\n" +
            "║                                                              ║\n" +
            "║  💡 You can test your APIs using Swagger UI!               ║\n" +
            "║                                                              ║\n" +
            "╚══════════════════════════════════════════════════════════════╝\n");
    }
}
package mz.uem.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * UEM Events Backend - Main Application Class
 * 
 * Backend robusto e profissional para gerenciamento de eventos da
 * Universidade Eduardo Mondlane (UEM).
 * 
 * @author UEM Development Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class UemEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UemEventsApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("🚀 UEM Events Backend is running!");
        System.out.println("📚 Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("🗄️  H2 Console: http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }
}

package com.mentorconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class MentorConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MentorConnectApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onAppReady() {
        System.out.println("\n" +
            "================================================================================\n" +
            "  🚀 MentorConnect Application is LIVE & READY!\n" +
            "  👉 Web Dashboard:    http://localhost:8080/\n" +
            "  👉 Swagger API Docs: http://localhost:8080/swagger-ui.html\n" +
            "  👉 Postman JSON:     http://localhost:8080/MentorConnect.postman_collection.json\n" +
            "================================================================================\n"
        );
    }
}

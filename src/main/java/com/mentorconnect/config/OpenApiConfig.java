package com.mentorconnect.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mentorConnectOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MentorConnect — Alumni-Student Mentorship API")
                        .description("Backend REST API that matches students to alumni mentors based on shared interest tags and tracks session activity.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MentorConnect Platform")
                                .email("support@mentorconnect.edu")));
    }
}

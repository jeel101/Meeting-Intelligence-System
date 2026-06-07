package com.hintro.meeting_intelligence.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Meeting Intelligence API",
                version = "1.0.0",
                description = "Hintro Backend/Fullstack Engineering Internship Assignment — Jeel Shah. " +
                        "AI-powered meeting intelligence service with transcript analysis, " +
                        "citation-grounded insights, action item tracking, and automated reminders.",
                contact = @Contact(name = "Jeel Shah", email = "jeelshah408@gmail.com")
        ),
        servers = {
                @Server(url = "/", description = "Current server")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter JWT token from /api/auth/login response"
)
public class SwaggerConfig {

}

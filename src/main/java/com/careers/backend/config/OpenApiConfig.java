package com.careers.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Careers Platform",
                version = "1.0.0",
                description = """
            REST API for managing Job Adverts.
            
            Features:
            - User creation
            - User lookup
            - User updates
            - Secure endpoints
            """,
                contact = @Contact(
                        name = "Visura Chandula",
                        email = "visurachandula@gmail.com"
                )
        ),
        servers = {
                @Server(url = "https://careers-backend-5enq.onrender.com", description = "Render Production")
        }
)
public class OpenApiConfig {
    // No additional code needed
}
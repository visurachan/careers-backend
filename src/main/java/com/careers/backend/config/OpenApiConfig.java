package com.careers.backend.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.servers.Server;

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
                @Server(url = "http://localhost:8080", description = "Local"),
                @Server(url = "https://api.myapp.com", description = "Production")
        }
)
public class OpenApiConfig {
}

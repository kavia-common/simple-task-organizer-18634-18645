package com.example.backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Simple Task Organizer API",
                version = "0.1.0",
                description = "REST API for managing tasks and authentication (MongoDB backend).",
                contact = @Contact(name = "Task API", email = "support@example.com")
        ),
        servers = {@Server(url = "/", description = "Default Server")}
)
public class backendApplication {

    public static void main(String[] args) {
        SpringApplication.run(backendApplication.class, args);
    }

    // Allow CORS for local frontend usage; adjust origins as needed via env in the future
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

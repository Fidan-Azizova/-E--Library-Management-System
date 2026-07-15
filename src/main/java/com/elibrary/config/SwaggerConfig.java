package com.elibrary.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI configuration.
 *
 * HOW TO ADD THE JWT TOKEN IN SWAGGER UI:
 * 1. Run the application and open http://localhost:8080/swagger-ui.html
 * 2. Call POST /api/auth/register to create a user, then POST /api/auth/login to obtain a JWT token.
 * 3. Click the green "Authorize" button at the top-right of the Swagger UI page.
 * 4. In the dialog box that appears (scheme name "bearerAuth"), paste ONLY the raw token
 *    (Swagger automatically adds the "Bearer " prefix for you).
 * 5. Click "Authorize", then "Close". Every subsequent request will now include the
 *    Authorization: Bearer <token> header automatically.
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Library Management System API")
                        .description("Final Project - Java Spring Boot Backend Development. " +
                                "Books & Categories management with JWT authentication and role-based access control.")
                        .version("1.0.0")
                        .contact(new Contact().name("E-Library Team")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter ONLY the JWT token (without 'Bearer ' prefix). " +
                                        "Obtain it from POST /api/auth/login.")));
    }
}

package com.elibrary.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Registration request payload")
public class RegisterRequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(example = "john_doe")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be a valid email address")
    @Schema(example = "john@example.com")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    @Schema(example = "P@ssw0rd123")
    private String password;

    @Schema(description = "Requested roles. If empty, ROLE_USER is assigned by default.",
            example = "[\"ROLE_ADMIN\"]")
    private Set<String> roles;
}

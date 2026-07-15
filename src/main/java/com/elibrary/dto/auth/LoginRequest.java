package com.elibrary.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Login request payload")
public class LoginRequest {

    @NotBlank(message = "Username is mandatory")
    @Schema(example = "john_doe")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Schema(example = "P@ssw0rd123")
    private String password;
}

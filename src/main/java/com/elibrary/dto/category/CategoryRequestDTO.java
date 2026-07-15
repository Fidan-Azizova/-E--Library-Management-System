package com.elibrary.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload used to create or update a category")
public class CategoryRequestDTO {

    @NotBlank(message = "Category name is mandatory")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    @Schema(example = "Elmi")
    private String name;
}

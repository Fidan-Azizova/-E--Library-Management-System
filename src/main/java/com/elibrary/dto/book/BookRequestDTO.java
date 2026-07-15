package com.elibrary.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload used to create or update a book")
public class BookRequestDTO {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title can be at most 255 characters")
    @Schema(example = "Clean Code")
    private String title;

    @NotBlank(message = "Author cannot be empty")
    @Size(max = 150, message = "Author can be at most 150 characters")
    @Schema(example = "Robert C. Martin")
    private String author;

    @NotBlank(message = "ISBN is mandatory")
    @Pattern(regexp = "\\d{13}", message = "ISBN must be exactly 13 digits")
    @Schema(example = "9780132350884")
    private String isbn;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.1", inclusive = true, message = "Price must be at least 0.1")
    @Schema(example = "25.99")
    private BigDecimal price;

    @NotNull(message = "Category id is mandatory")
    @Min(value = 1, message = "Category id must be a positive number")
    @Schema(example = "1")
    private Long categoryId;
}

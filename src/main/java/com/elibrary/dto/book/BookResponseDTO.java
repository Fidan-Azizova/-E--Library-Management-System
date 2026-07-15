package com.elibrary.dto.book;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String categoryName;
}

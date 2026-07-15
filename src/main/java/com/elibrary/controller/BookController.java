package com.elibrary.controller;

import com.elibrary.dto.book.BookRequestDTO;
import com.elibrary.dto.book.BookResponseDTO;
import com.elibrary.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Book management endpoints (CRUD, pagination, sorting)")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Add a new book", description = "Requires ROLE_ADMIN")
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Get all books",
            description = "Supports pagination and sorting, e.g. /api/books?page=0&size=10&sort=title,asc")
    public ResponseEntity<Page<BookResponseDTO>> getAll(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(bookService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a book", description = "Requires ROLE_ADMIN")
    public ResponseEntity<BookResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BookRequestDTO dto) {
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete a book", description = "Requires ROLE_ADMIN")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

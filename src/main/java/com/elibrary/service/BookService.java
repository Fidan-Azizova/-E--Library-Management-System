package com.elibrary.service;

import com.elibrary.dto.book.BookRequestDTO;
import com.elibrary.dto.book.BookResponseDTO;
import com.elibrary.entity.Book;
import com.elibrary.entity.Category;
import com.elibrary.exception.DuplicateResourceException;
import com.elibrary.exception.ResourceNotFoundException;
import com.elibrary.mapper.BookMapper;
import com.elibrary.repository.BookRepository;
import com.elibrary.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookResponseDTO create(BookRequestDTO dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateResourceException("A book with ISBN " + dto.getIsbn() + " already exists");
        }
        Category category = findCategoryOrThrow(dto.getCategoryId());

        Book book = bookMapper.toEntity(dto);
        book.setCategory(category);

        Book saved = bookRepository.save(book);
        return bookMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    @Transactional(readOnly = true)
    public BookResponseDTO getById(Long id) {
        Book book = findBookOrThrow(id);
        return bookMapper.toDto(book);
    }

    @Transactional
    public BookResponseDTO update(Long id, BookRequestDTO dto) {
        Book book = findBookOrThrow(id);

        bookRepository.findByIsbn(dto.getIsbn()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("A book with ISBN " + dto.getIsbn() + " already exists");
            }
        });

        Category category = findCategoryOrThrow(dto.getCategoryId());

        bookMapper.updateEntityFromDto(dto, book);
        book.setCategory(category);

        Book updated = bookRepository.save(book);
        return bookMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        Book book = findBookOrThrow(id);
        bookRepository.delete(book);
    }

    private Book findBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }
}

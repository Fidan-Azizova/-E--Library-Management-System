package com.elibrary.service;

import com.elibrary.dto.category.CategoryRequestDTO;
import com.elibrary.dto.category.CategoryResponseDTO;
import com.elibrary.entity.Category;
import com.elibrary.exception.DuplicateResourceException;
import com.elibrary.exception.ResourceNotFoundException;
import com.elibrary.mapper.CategoryMapper;
import com.elibrary.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new DuplicateResourceException("Category already exists with name: " + dto.getName());
        }
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO getById(Long id) {
        Category category = findCategoryOrThrow(id);
        return categoryMapper.toDto(category);
    }

    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = findCategoryOrThrow(id);

        categoryRepository.findByNameIgnoreCase(dto.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new DuplicateResourceException("Category already exists with name: " + dto.getName());
            }
        });

        categoryMapper.updateEntityFromDto(dto, category);
        Category updated = categoryRepository.save(category);
        return categoryMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findCategoryOrThrow(id);
        categoryRepository.delete(category);
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}

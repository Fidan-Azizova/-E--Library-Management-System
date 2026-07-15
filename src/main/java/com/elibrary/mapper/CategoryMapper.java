package com.elibrary.mapper;

import com.elibrary.dto.category.CategoryRequestDTO;
import com.elibrary.dto.category.CategoryResponseDTO;
import com.elibrary.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryResponseDTO toDto(Category category);

    Category toEntity(CategoryRequestDTO dto);

    void updateEntityFromDto(CategoryRequestDTO dto, @MappingTarget Category category);
}

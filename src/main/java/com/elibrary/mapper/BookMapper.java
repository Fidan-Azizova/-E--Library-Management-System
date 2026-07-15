package com.elibrary.mapper;

import com.elibrary.dto.book.BookRequestDTO;
import com.elibrary.dto.book.BookResponseDTO;
import com.elibrary.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(source = "category.name", target = "categoryName")
    BookResponseDTO toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Book toEntity(BookRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(BookRequestDTO dto, @MappingTarget Book book);
}

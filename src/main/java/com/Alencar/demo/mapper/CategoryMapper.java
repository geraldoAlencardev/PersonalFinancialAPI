package com.Alencar.demo.mapper;

import com.Alencar.demo.dto.category.CategoryCreateDTO;
import com.Alencar.demo.dto.category.CategoryResponseDTO;
import com.Alencar.demo.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateDTO dto) {
        return Category.builder()
                .name(dto.name())
                .color(dto.color())
                .icon(dto.icon())
                .build();
    }

    public CategoryResponseDTO CategoryResponseDTO(Category entity) {
        return new CategoryResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getColor(),
                entity.getIcon());
    }
}

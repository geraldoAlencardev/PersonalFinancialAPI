package com.Alencar.demo.controller;

import com.Alencar.demo.dto.category.CategoryCreateDTO;
import com.Alencar.demo.dto.category.CategoryResponseDTO;
import com.Alencar.demo.mapper.CategoryMapper;
import com.Alencar.demo.model.Category;
import com.Alencar.demo.model.User;
import com.Alencar.demo.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper  categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAll(
            @AuthenticationPrincipal User loggedUser
    ){
        List<Category> categories = categoryService.findAllByUser(loggedUser.getId());
        List<CategoryResponseDTO> response = categories.stream().map(categoryMapper::CategoryResponseDTO).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @AuthenticationPrincipal User loggedUser,
            @Valid @RequestBody CategoryCreateDTO dto
            ){
        Category category = categoryMapper.toEntity(dto);
        Category savedCategory = categoryService.createCategory(category, loggedUser.getId());
        CategoryResponseDTO response = categoryMapper.CategoryResponseDTO(savedCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Long id
    ){
        categoryService.deleteCategory(id, loggedUser.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

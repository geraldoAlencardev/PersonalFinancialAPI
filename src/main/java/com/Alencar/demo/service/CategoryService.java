package com.Alencar.demo.service;

import com.Alencar.demo.infrastructure.exceptions.*;
import com.Alencar.demo.model.Category;
import com.Alencar.demo.model.User;
import com.Alencar.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> findAllByUser(Long userId) {
        return categoryRepository.findAllGlobalAndByUser(userId);
    }

    // sem utilização até o momento
    @Transactional(readOnly = true)
    public Category findById(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        if(category.getUser() != null && !category.getUser().getId().equals(userId)){
            throw new ResourceNotFoundException("Categoria não encontrada ou não pertece ao usuario");
        }
        return category;
    }

    @Transactional
    public Category createCategory(Category category, Long userId) {
        category.setUser(User.builder().id(userId).build());

        boolean categoryExists = categoryRepository.existsByNameAndUserOrGlobal(category.getName(), userId);
        if (categoryExists) {
            throw new BusinessException("Categoria já cadastrada");
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));

        if(category.getUser() == null){
            throw new BusinessException("Categoria do sistema não podem ser removidas");
        }

        if(!category.getUser().getId().equals(userId)){
            throw new ResourceNotFoundException("Categoria não encontrada ou não pertence ao usuário.");
        }

        if(category.getTransactions() != null && !category.getTransactions().isEmpty()){
            throw new BusinessException("Não é possível excluir uma categoria que possui transações vinculadas. " +
                    "Dica: Altere as transações para outra categoria antes de excluir.");
        }

        categoryRepository.delete(category);

    }
}

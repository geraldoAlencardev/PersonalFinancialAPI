package com.Alencar.demo.repository;

import com.Alencar.demo.model.Category;
import com.Alencar.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIsNull();

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT c FROM Category c WHERE c.user IS NULL OR c.user.id = :userId")
    List<Category> findAllGlobalAndByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name AND (c.user IS NULL OR c.user.id = :userId)")
    boolean existsByNameAndUserOrGlobal(@Param("name") String name, @Param("userId") Long userId);
}

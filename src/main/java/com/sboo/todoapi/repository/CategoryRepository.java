package com.sboo.todoapi.repository;

import com.sboo.todoapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);

}

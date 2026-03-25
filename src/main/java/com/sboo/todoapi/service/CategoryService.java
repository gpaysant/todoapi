package com.sboo.todoapi.service;

import com.sboo.todoapi.dto.CategoryRequest;
import com.sboo.todoapi.dto.CategoryResponse;
import com.sboo.todoapi.exception.TodoNotFoundException;
import com.sboo.todoapi.model.Category;
import com.sboo.todoapi.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(id));
    }

    public CategoryResponse getCategoryResponseById(Long id) {
        return toResponse(getCategoryById(id));
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new IllegalArgumentException(String.format("name %s alread exist %n", request.name()));
        }
        return toResponse(categoryRepository.save(toEntity(request)));
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category categoryExisting = getCategoryById(id);
        categoryExisting.setName(request.name());
        categoryExisting.setDescription(request.description());
        return toResponse(categoryRepository.save(categoryExisting));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    private Category toEntity(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setDescription(categoryRequest.description());
        category.setName(categoryRequest.name());
        return category;
    }

    private CategoryResponse toResponse (Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getTodos().size());
    }
}

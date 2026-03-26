package com.sboo.todoapi.service;

import com.sboo.todoapi.dto.PageResponse;
import com.sboo.todoapi.dto.TodoRequest;
import com.sboo.todoapi.dto.TodoResponse;
import com.sboo.todoapi.exception.TodoNotFoundException;
import com.sboo.todoapi.model.Category;
import com.sboo.todoapi.model.Todo;
import com.sboo.todoapi.repository.CategoryRepository;
import com.sboo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;// final obligatoire !
    // Pas besoin du constructeur, Lombok le génère

    private final CategoryRepository categoryRepository;

    public List<TodoResponse> getAllTodos() {
        return todoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    public TodoResponse getTodoResponseById(Long id) {
        return toResponse(getTodoById(id));
    }

    public TodoResponse createTodo(TodoRequest todoRequest) {
        return toResponse(todoRepository.save(toEntity(todoRequest)));
    }

    public TodoResponse updateTodo(Long id, TodoRequest todoRequest) {
        Todo existing = getTodoById(id);
        existing.setTitle(todoRequest.title());
        existing.setDescription(todoRequest.description());
        existing.setCompleted(todoRequest.completed());
        existing.setDueDate(todoRequest.dueDate());
        return toResponse(todoRepository.save(existing));
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    public List<TodoResponse> getCompletedTodos() {
        return todoRepository.findByCompletedTrue().stream().map(this::toResponse).toList();
    }

    public List<TodoResponse> searchByTitle(String keyword) {
        return todoRepository.findByTitleContainingIgnoreCase(keyword).stream().map(this::toResponse).toList();
    }

    public List<TodoResponse> getTodosDueBefore(LocalDate date) {
        return todoRepository.findByDueDateBefore(date).stream().map(this::toResponse).toList();
    }

    public PageResponse<TodoResponse> getAllTodos(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Todo> todosPage = todoRepository.findAll(pageable);
        return toPageResponse(todosPage);
    }

    public List<TodoResponse> getTodosByCategoryId(Long categoryId) {
        return todoRepository.findByCategoryId(categoryId).stream().map(this::toResponse).toList();
    }

    private Todo toEntity(TodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setCompleted(request.completed() != null ? request.completed() : false);
        todo.setDueDate(request.dueDate());
        if (request.categoryId() != null) {
            Category categoryExisting = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new TodoNotFoundException(request.categoryId()));
            todo.setCategory(categoryExisting);
        }
        return todo;
    }

    private TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted(),
                todo.getCreatedAt(),
                todo.getDueDate(),
                todo.getCategory() != null ? todo.getCategory().getName() : null
        );
    }

    private PageResponse<TodoResponse> toPageResponse(Page<Todo> page) {
        return new PageResponse<>(
                page.stream().map(this::toResponse).toList(),
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isLast()
        );
    }
}

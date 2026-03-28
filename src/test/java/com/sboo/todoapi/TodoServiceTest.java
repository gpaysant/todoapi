package com.sboo.todoapi;

import com.sboo.todoapi.dto.PageResponse;
import com.sboo.todoapi.dto.TodoRequest;
import com.sboo.todoapi.dto.TodoResponse;
import com.sboo.todoapi.exception.TodoNotFoundException;
import com.sboo.todoapi.model.Category;
import com.sboo.todoapi.model.Todo;
import com.sboo.todoapi.repository.CategoryRepository;
import com.sboo.todoapi.repository.TodoRepository;
import com.sboo.todoapi.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TodoService todoService;

    // Données de test réutilisables
    private Todo todo;
    private TodoRequest todoRequest;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Work");
        category.setDescription("All informations for work");
        todo = new Todo();
        todo.setId(1);
        todo.setTitle("Test todo");
        todo.setDescription("First todo to test");
        todo.setCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setDueDate(LocalDate.now().plusDays(7));
        todo.setCategory(category);

        todoRequest = new TodoRequest("Test todo", "First todo to test", false, LocalDate.now().plusDays(7), 1L);
    }

    @Test
    void shouldReturnTodoWhenIdExists() {
        // Arrange : simuler que le repository retourne le todo
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        // Act : appeler la méthode
        TodoResponse result = todoService.getTodoResponseById(1L);

        // Assert : vérifier le résultat
        assertThat(result.title()).isEqualTo("Test todo");
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void shouldThrowTodoNotFoundExceptionWhenIdNotExist() {
        when(todoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows( TodoNotFoundException.class,
                () -> todoService.getTodoResponseById(2L)
        );
    }

    @Test
    void shouldReturnTodoWhenCreateTodo() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        TodoResponse result = todoService.createTodo(todoRequest);

        assertThat(result.title()).isEqualTo("Test todo");
        assertThat(result.categoryName()).isEqualTo("Work");
    }

    @Test
    void shouldReturnTodoWhenUpdateTodo() {
        TodoRequest updatedRequest = new TodoRequest(
                "Updated title", "Updated description", false,
                LocalDate.now().plusDays(7), 1L
        );
        Todo updatedTodo = new Todo();
        updatedTodo.setId(1);
        updatedTodo.setTitle("Updated title");
        updatedTodo.setDescription("Updated description");
        updatedTodo.setCompleted(false);
        updatedTodo.setDueDate(LocalDate.now().plusDays(7));
        updatedTodo.setCategory(category);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(updatedTodo);

        TodoResponse result = todoService.updateTodo(1L, updatedRequest);

        assertThat(result.title()).isEqualTo("Updated title");
    }

    @Test
    void shouldReturnVoidWhenDeleteTodo() {
        todoService.deleteTodo(1L);

        verify(todoRepository).deleteById(1L);
    }

    @Test
    void shouldReturnListTodosWhenGetAllTodos() {
        Page<Todo> page = new PageImpl<Todo>(
                List.of(todo),
                PageRequest.of(0, 3, Sort.by("title").descending()),
                1);
        when(todoRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageResponse<TodoResponse> pageResponse = todoService.getAllTodos(0, 3, "title", "desc");

        assertThat(pageResponse.totalElements()).isEqualTo(1);
        assertThat(pageResponse.pageSize()).isEqualTo(3);
        assertThat(pageResponse.content()).hasSize(1);
    }

}

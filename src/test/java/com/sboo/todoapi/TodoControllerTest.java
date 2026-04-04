package com.sboo.todoapi;

import com.sboo.todoapi.dto.TodoRequest;
import com.sboo.todoapi.model.Category;
import com.sboo.todoapi.model.Todo;
import com.sboo.todoapi.repository.CategoryRepository;
import com.sboo.todoapi.repository.TodoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Lance le contexte Spring complet
@AutoConfigureMockMvc // Configure MockMvc automatiquement
@Transactional // Rollback après chaque test — BDD propre
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Convertit objets ↔ JSON

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Todo todo;

    @BeforeEach
    void setUp() {

        category = new Category();
        category.setName("Test category");
        category.setDescription("First category to test");
        category = categoryRepository.save(category);

        todo = new Todo();
        todo.setTitle("Test todo");
        todo.setDescription("First todo to test");
        todo.setCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setDueDate(LocalDate.now().plusDays(7));
        todo.setCategory(category);

        todo= todoRepository.save(todo);
    }

    @Test
    void shouldCreateTodoSuccessfully() throws Exception {
        TodoRequest request = new TodoRequest(
                "Nouveau todo", "Description", false,
                LocalDate.now().plusDays(7), category.getId()
        );

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Nouveau todo"))
                .andExpect(jsonPath("$.categoryName").value("Test category"));
    }

    @Test
    void shouldFailToCreateTodoWhenTitleIsBlank() throws Exception {
        TodoRequest request = new TodoRequest(
                "", "Description", false,
                LocalDate.now().plusDays(7), category.getId()
        );

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllTodosWithPaginationSuccessfully() throws Exception {
        mockMvc.perform(get("/api/todos")
                        .param("page", "0")
                        .param("size", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldGetTodoByIdSuccessfully() throws Exception {
        mockMvc.perform(get("/api/todos/{id}", todo.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test todo"));
    }

    @Test
    void shouldGetTodoByIdFailed() throws Exception {
        mockMvc.perform(get("/api/todos/{id}", 99999))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTodoSuccesfully() throws Exception {
        TodoRequest request = new TodoRequest(
                "Todo updated", "Todo updated again", true,
                LocalDate.now().plusDays(7), category.getId()
        );

        mockMvc.perform(put("/api/todos/{id}", todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Todo updated"));
    }

    @Test
    void shouldDeleteTodoSuccesfully() throws Exception {
        mockMvc.perform(delete("/api/todos/{id}", todo.getId()))
                .andExpect(status().isNoContent());
    }

}

package com.sboo.todoapi.controller;

import com.sboo.todoapi.dto.PageResponse;
import com.sboo.todoapi.dto.TodoRequest;
import com.sboo.todoapi.dto.TodoResponse;
import com.sboo.todoapi.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/todos")
@RestController
public class TodoController {

    private final TodoService todoService;

    /*@GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodos () {
        return ResponseEntity.ok(todoService.getAllTodos());
    }*/
    @GetMapping
    public ResponseEntity<PageResponse<TodoResponse>> getAllTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(todoService.getAllTodos(page, size, sortBy, sortDir));
    }


    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable long id) {
        return ResponseEntity.ok(todoService.getTodoResponseById(id));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo (@Valid @RequestBody TodoRequest todoRequest) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(todoRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo (@PathVariable long id, @Valid @RequestBody TodoRequest todoRequest) {
        return ResponseEntity.ok(todoService.updateTodo(id, todoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo (@PathVariable long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/completed")
    public ResponseEntity<List<TodoResponse>> getTodoCompleted () {
        return ResponseEntity.ok(todoService.getCompletedTodos());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TodoResponse>> getByTitle (@RequestParam String keyword) {
        return ResponseEntity.ok(todoService.searchByTitle(keyword));
    }

    @GetMapping("/due")
    public ResponseEntity<List<TodoResponse>> getByDeadline (@RequestParam LocalDate dateDateLine) {
        return ResponseEntity.ok(todoService.getTodosDueBefore(dateDateLine));
    }
}

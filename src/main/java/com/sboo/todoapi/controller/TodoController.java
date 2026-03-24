package com.sboo.todoapi.controller;

import com.sboo.todoapi.model.Todo;
import com.sboo.todoapi.service.TodoService;
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

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos () {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo (@RequestBody Todo todo) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo (@PathVariable long id, @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.updateTodo(id, todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo (@PathVariable long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Todo>> getTodoCompleted () {
        return ResponseEntity.ok(todoService.getCompletedTodos());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Todo>> getByTitle (@RequestParam String keyword) {
        return ResponseEntity.ok(todoService.searchByTitle(keyword));
    }

    @GetMapping("/due")
    public ResponseEntity<List<Todo>> getByDeadline (@RequestParam LocalDate dateDateLine) {
        return ResponseEntity.ok(todoService.getTodosDueBefore(dateDateLine));
    }
}

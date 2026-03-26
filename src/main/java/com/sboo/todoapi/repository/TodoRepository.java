package com.sboo.todoapi.repository;

import com.sboo.todoapi.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByCompletedTrue();
    List<Todo> findByTitleContainingIgnoreCase(String keyword);
    List<Todo> findByDueDateBefore(LocalDate dueDate);
    Page<Todo> findAll(Pageable pageable);
    Page<Todo> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    List<Todo> findByCategoryId(Long categoryId);
}
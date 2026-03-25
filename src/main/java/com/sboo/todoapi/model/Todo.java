package com.sboo.todoapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = true)
    private String description;
    private boolean completed = false;
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createdAt;
    @Column(columnDefinition = "DATE")
    private LocalDate dueDate;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

package com.mipt.sem2.hw1.todolist.model;

import com.mipt.sem2.hw1.todolist.enums.Priority;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(length = 500)
  private String description;

  private boolean completed;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "due_date")
  private LocalDate dueDate;

  //priority добавил Enumerated
  @Enumerated(EnumType.STRING)
  @Column(length = 10)
  private Priority priority;

  //эти в Json еределываю
  @Column(columnDefinition = "jsonb")
  @Convert(converter = SetToStringJsonConverter.class)
  private Set<String> tags;

  @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<TaskAttachment> attachments;
}
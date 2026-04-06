package com.mipt.sem2.hw1.todolist.repository;

import com.mipt.sem2.hw1.todolist.model.Task;
import com.mipt.sem2.hw1.todolist.enums.Priority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class TaskRepositoryTest {

  @Autowired
  private TaskRepository taskRepository;

  @Test
  void shouldFindTasksDueWithinNextDays() {
    Task task = new Task();
    task.setTitle("Due soon");
    task.setDueDate(LocalDate.now().plusDays(3));
    task.setPriority(Priority.HIGH);
    task.setCompleted(false);
    taskRepository.save(task);

    LocalDate start = LocalDate.now();
    LocalDate end = LocalDate.now().plusDays(7);
    var found = taskRepository.findTasksDueWithinNextDays(start, end);
    assertThat(found).hasSize(1);
  }

  @Test
  void shouldFindByCompletedAndPriority() {
    Task t1 = new Task(); t1.setTitle("Task1"); t1.setCompleted(true); t1.setPriority(Priority.HIGH);
    Task t2 = new Task(); t2.setTitle("Task2"); t2.setCompleted(false); t2.setPriority(Priority.HIGH);
    taskRepository.saveAll(Set.of(t1, t2));

    var completedHigh = taskRepository.findByCompletedAndPriority(true, Priority.HIGH);
    assertThat(completedHigh).hasSize(1);
  }
}
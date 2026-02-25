package com.mipt.sem2.todolist.config;

import com.mipt.sem2.todolist.repository.StubTaskRepository;
import com.mipt.sem2.todolist.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public TaskRepository stubTaskRepository() {
    return new StubTaskRepository();
  }
}

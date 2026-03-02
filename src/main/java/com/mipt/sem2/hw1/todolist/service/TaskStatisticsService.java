package com.mipt.sem2.hw1.todolist.service;

import com.mipt.sem2.hw1.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TaskStatisticsService {
  private final TaskRepository primaryRepository;
  private final TaskRepository stubRepository;

  @Autowired
  public TaskStatisticsService(
      TaskRepository primaryRepository,
      @Qualifier("stubTaskRepository") TaskRepository stubRepository) {
    this.primaryRepository = primaryRepository;
    this.stubRepository = stubRepository;
  }

  public void printStats() {
    System.out.println("Primary repository size: " + primaryRepository.findAll().size());
    System.out.println("Stub repository size: " + stubRepository.findAll().size());
  }
}
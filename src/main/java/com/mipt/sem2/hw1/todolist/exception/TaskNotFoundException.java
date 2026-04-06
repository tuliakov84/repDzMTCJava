package com.mipt.sem2.hw1.todolist.exception;

public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(String message) { super(message); }
}

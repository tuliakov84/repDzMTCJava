package com.mipt.sem2.hw1.todolist.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Scope("prototype")
public class PrototypeScopedBean {
  private final String id = UUID.randomUUID().toString();

  public String getId() {
    return id;
  }
}
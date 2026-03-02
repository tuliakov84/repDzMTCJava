package com.mipt.sem2.hw1.todolist.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedBean {
  private final String requestId = UUID.randomUUID().toString();
  private final LocalDateTime startTime = LocalDateTime.now();

  public String getRequestId() {
    return requestId;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public long getProcessingTime() {
    return java.time.Duration.between(startTime, LocalDateTime.now()).toMillis();
  }
}
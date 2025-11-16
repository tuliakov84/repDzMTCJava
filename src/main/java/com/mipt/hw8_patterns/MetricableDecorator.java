package com.mipt.hw8_patterns;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

class MetricableDecorator implements DataService {
  private final DataService delegate;

  public MetricableDecorator(DataService delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    Instant start = Instant.now();
    Optional<String> result = delegate.findDataByKey(key);
    Duration duration = Duration.between(start, Instant.now());
    MetricService.sendMetric(duration);
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    Instant start = Instant.now();
    delegate.saveData(key, data);
    Duration duration = Duration.between(start, Instant.now());
    MetricService.sendMetric(duration);
  }

  @Override
  public boolean deleteData(String key) {
    Instant start = Instant.now();
    boolean result = delegate.deleteData(key);
    Duration duration = Duration.between(start, Instant.now());
    MetricService.sendMetric(duration);
    return result;
  }

  public static class MetricService {
    public static void sendMetric(Duration duration) {
      System.out.println("Метод выполнялся: " + duration.toString());
    }
  }
}
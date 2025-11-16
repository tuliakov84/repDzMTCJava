package com.mipt.hw7_patterns;

import java.util.Optional;

class LoggingDecorator implements DataService {
  private final DataService delegate;

  public LoggingDecorator(DataService delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    System.out.println("findDataByKey was called with key: " + key);
    Optional<String> result = delegate.findDataByKey(key);
    System.out.println("findDataByKey returned: " + result.orElse("null"));
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    System.out.println("saveData was called with key: " + key + " and data: " + data);
    delegate.saveData(key, data);
    System.out.println("data is saved");
  }

  @Override
  public boolean deleteData(String key) {
    System.out.println("deleteData was called with key: " + key);
    boolean result = delegate.deleteData(key);
    System.out.println("deleteData returned: " + result);
    return result;
  }
}

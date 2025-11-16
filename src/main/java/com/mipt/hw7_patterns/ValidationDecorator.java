package com.mipt.hw7_patterns;

import java.util.Optional;

class ValidationDecorator implements DataService {
  private final DataService delegate;

  public ValidationDecorator(DataService delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    validateKey(key);
    return delegate.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    validateKey(key);
    validateData(data);
    delegate.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    validateKey(key);
    return delegate.deleteData(key);
  }

  private void validateKey(String key) {
    if (key == null || key.trim().isEmpty()) {
      throw new IllegalArgumentException("Key cannot be null or empty");
    }
  }

  private void validateData(String data) {
    if (data == null || data.trim().isEmpty()) {
      throw new IllegalArgumentException("Data cannot be null or empty");
    }
  }
}

package com.mipt.hw8_patterns;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class CachingDecorator implements DataService {
  private final DataService delegate;
  private final Map<String, String> cache = new HashMap<>();

  public CachingDecorator(DataService delegate) {
    this.delegate = delegate;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    if (cache.containsKey(key)) {
      return Optional.of(cache.get(key));
    }
    Optional<String> result = delegate.findDataByKey(key);
    if (result.isPresent()) {
      cache.put(key, result.get());
    }
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    delegate.saveData(key, data);
    cache.put(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    boolean deleted = delegate.deleteData(key);
    if (deleted) {
      cache.remove(key);
    }
    return deleted;
  }
}

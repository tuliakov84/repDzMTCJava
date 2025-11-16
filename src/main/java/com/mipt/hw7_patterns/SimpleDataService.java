package com.mipt.hw7_patterns;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Базовый интерфейс сервиса данных
interface DataService {
  Optional<String> findDataByKey(String key);

  void saveData(String key, String data);

  boolean deleteData(String key);
}

// Базовая реализация - работа с памятью
class SimpleDataService implements DataService {
  private Map<String, String> storage = new HashMap<>();

  @Override
  public Optional<String> findDataByKey(String key) {
    return Optional.ofNullable(storage.get(key));
  }

  @Override
  public void saveData(String key, String data) {
    storage.put(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    return storage.remove(key) != null;
  }
}

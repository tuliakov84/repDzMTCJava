package com.mipt.hw7_patterns;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CachingDecoratorTest {
  @Test
  void shouldCacheResult() {
    DataService testDataservice = new SimpleDataService();
    CachingDecorator decorator = new CachingDecorator(testDataservice);

    decorator.saveData("test", "value");

    Optional<String> first = decorator.findDataByKey("test");
    Optional<String> second = decorator.findDataByKey("test");

    assertTrue(first.isPresent());
    assertTrue(second.isPresent());
    assertEquals(first.get(), second.get());

    decorator.deleteData("test");
    Optional<String> result = decorator.findDataByKey("test");
    assertThrows(NoSuchElementException.class, () -> result.get());
  }
}
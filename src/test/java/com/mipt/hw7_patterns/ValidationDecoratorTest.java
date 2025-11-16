package com.mipt.hw7_patterns;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationDecoratorTest {

  @Test
  void saveDataTest() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.saveData(null, "data")
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void saveData_shouldThrowWhenKeyIsEmpty() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.saveData("", "data")
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void saveData_shouldThrowWhenKeyIsBlank() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.saveData("   ", "data")
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void saveData_shouldThrowWhenDataIsNull() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.saveData("key", null)
    );
    assertEquals("Data cannot be null or empty", exception.getMessage());
  }

  @Test
  void saveData_shouldWorkWithValidInputs() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    assertDoesNotThrow(() -> testDataService.saveData("key", "data"));
  }

  @Test
  void findDataByKey_shouldThrowWhenKeyIsNull() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.findDataByKey(null)
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void findDataByKey_shouldThrowWhenKeyIsEmpty() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.findDataByKey("")
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void findDataByKey_shouldThrowWhenKeyIsBlank() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.findDataByKey("   ")
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void findDataByKey_shouldWorkWithValidKey() {
    DataService base = new SimpleDataService();
    base.saveData("key", "value");
    DataService testDataService = new ValidationDecorator(base);

    assertDoesNotThrow(() -> {
      var result = testDataService.findDataByKey("key");
      assertTrue(result.isPresent());
      assertEquals("value", result.get());
    });
  }

  @Test
  void deleteData_shouldThrowWhenKeyIsNull() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.deleteData(null)
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void deleteData_shouldThrowWhenKeyIsEmpty() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.deleteData("")
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void deleteData_shouldThrowWhenKeyIsBlank() {
    DataService testDataService = new ValidationDecorator(new SimpleDataService());
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> testDataService.deleteData("   ")
    );
    assertEquals("Key cannot be null or empty", exception.getMessage());
  }

  @Test
  void deleteData_shouldWorkWithValidKey() {
    DataService base = new SimpleDataService();
    base.saveData("key", "value");
    DataService testDataService = new ValidationDecorator(base);

    assertDoesNotThrow(() -> {
      boolean deleted = testDataService.deleteData("key");
      assertTrue(deleted);
    });
  }
}
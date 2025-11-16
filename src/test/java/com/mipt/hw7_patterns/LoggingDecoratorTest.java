package com.mipt.hw7_patterns;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class LoggingDecoratorTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  void saveDataTest() {
    DataService testDataService = new SimpleDataService();
    LoggingDecorator decorator = new LoggingDecorator(testDataService);

    decorator.saveData("key", "data");

    String output = outContent.toString();
    assertTrue(output.contains("saveData was called with key: key and data: data"));
    assertTrue(output.contains("data is saved"));
  }

  @Test
  void findDataByKeyNotNull() {
    DataService testDataService = new SimpleDataService();
    LoggingDecorator decorator = new LoggingDecorator(testDataService);

    decorator.saveData("key", "data");

    Optional<String> result = decorator.findDataByKey("key");

    assertTrue(result.isPresent());
    assertEquals("data", result.get());

    String output = outContent.toString();
    assertTrue(output.contains("findDataByKey was called with key: key"));
    assertTrue(output.contains("findDataByKey returned: data"));
  }

  @Test
  void findDataByKeyNull() {
    DataService testDataService = new SimpleDataService();
    LoggingDecorator decorator = new LoggingDecorator(testDataService);

    Optional<String> result = decorator.findDataByKey("key");

    String output = outContent.toString();
    assertTrue(output.contains("findDataByKey was called with key: key"));
    assertTrue(output.contains("findDataByKey returned: null"));
  }

  @Test
  void deleteDataSuccess() {
    DataService testDataService = new SimpleDataService();
    LoggingDecorator decorator = new LoggingDecorator(testDataService);

    decorator.saveData("key", "data");

    boolean deleted = decorator.deleteData("key");

    assertTrue(deleted);
    String output = outContent.toString();
    assertTrue(output.contains("deleteData was called with key: key"));
    assertTrue(output.contains("deleteData returned: true"));
  }

  @Test
  void deleteDataUnsuccess() {
    DataService testDataService = new SimpleDataService();
    LoggingDecorator decorator = new LoggingDecorator(testDataService);

    boolean deleted = decorator.deleteData("key");

    assertFalse(deleted);
    String output = outContent.toString();
    assertTrue(output.contains("deleteData was called with key: key"));
    assertTrue(output.contains("deleteData returned: false"));
  }
}
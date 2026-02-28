package com.mipt.hw8_patterns;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class MetricableDecoratorTest {

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
    DataService testDataService = new MetricableDecorator(new SimpleDataService());
    testDataService.saveData("key", "data");

    String output = outContent.toString();
    assertTrue(output.contains("Метод выполнялся: "));
  }

  @Test
  void findDataByKeyTest() {
    DataService testDataService = new SimpleDataService();
    testDataService.saveData("key", "data");
    MetricableDecorator decorator = new MetricableDecorator(testDataService);

    Optional<String> result = decorator.findDataByKey("key");

    assertTrue(result.isPresent());
    String output = outContent.toString();
    assertTrue(output.contains("Метод выполнялся: "));
  }

  @Test
  void findDataByKeyTestNotFound() {
    DataService testDataService = new MetricableDecorator(new SimpleDataService());
    Optional<String> result = testDataService.findDataByKey("missing");

    assertFalse(result.isPresent());
    String output = outContent.toString();
    assertTrue(output.contains("Метод выполнялся: "));
  }

  @Test
  void deleteDataTestSuccess() {
    DataService testDataService = new SimpleDataService();
    testDataService.saveData("key", "data");
    MetricableDecorator decorator = new MetricableDecorator(testDataService);

    boolean deleted = decorator.deleteData("key");

    assertTrue(deleted);
    String output = outContent.toString();
    assertTrue(output.contains("Метод выполнялся: "));
  }

  @Test
  void deleteDataTestUnsuccessful() {
    DataService testDataService = new MetricableDecorator(new SimpleDataService());
    boolean deleted = testDataService.deleteData("missing");

    assertFalse(deleted);
    String output = outContent.toString();
    assertTrue(output.contains("Метод выполнялся: "));
  }
}
package com.mipt;

import java.util.*;

import org.junit.jupiter.api.Test;

public class CollectionPerformanceTester {

  private static final int ELEMENTS_COUNT = 10000;

  @Test
  public void testPerformance() {
    System.out.println("=== Сравнение производительности ArrayList и LinkedList ===");
    System.out.printf("%-20s | %-15s | %-15s%n", "Операция", "ArrayList (мс)", "LinkedList (мс)");

    List<String> arrayList = new ArrayList<>();
    List<String> linkedList = new LinkedList<>();

    for (int i = 0; i < ELEMENTS_COUNT; i++) {
      arrayList.add("item" + i);
      linkedList.add("item" + i);
    }

    testAddToEnd(arrayList, "newItem");
    testAddToEnd(linkedList, "newItem");

    testAddToStart(arrayList, "newItem");
    testAddToStart(linkedList, "newItem");

    testInsertMiddle(arrayList, "newItem");
    testInsertMiddle(linkedList, "newItem");

    testGetByIndex(arrayList);
    testGetByIndex(linkedList);

    testRemoveFromStart(arrayList);
    testRemoveFromStart(linkedList);

    testRemoveFromEnd(arrayList);
    testRemoveFromEnd(linkedList);
  }

  private static void testAddToEnd(List<String> list, String item) {
    long start = System.nanoTime();
    for (int i = 0; i < 10000; i++) {
      list.add(item);
    }
    long end = System.nanoTime();
    long durationMs = (end - start) / 1_000_000;
    printResult("Добавление в конец", list.getClass().getSimpleName(), durationMs);
  }

  private static void testAddToStart(List<String> list, String item) {
    long start = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
      list.add(0, item);
    }
    long end = System.nanoTime();
    long durationMs = (end - start) / 1_000_000;
    printResult("Добавление в начало", list.getClass().getSimpleName(), durationMs);
  }

  private static void testInsertMiddle(List<String> list, String item) {
    long start = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
      list.add(list.size() / 2, item);
    }
    long end = System.nanoTime();
    long durationMs = (end - start) / 1_000_000;
    printResult("Вставка в середину", list.getClass().getSimpleName(), durationMs);
  }

  private static void testGetByIndex(List<String> list) {
    long start = System.nanoTime();
    for (int i = 0; i < 100000; i++) {
      list.get(i % list.size());
    }
    long end = System.nanoTime();
    long durationMs = (end - start) / 1_000_000;
    printResult("Доступ по индексу", list.getClass().getSimpleName(), durationMs);
  }

  private static void testRemoveFromStart(List<String> list) {
    long start = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
      list.remove(0);
    }
    long end = System.nanoTime();
    long durationMs = (end - start) / 1_000_000;
    printResult("Удаление из начала", list.getClass().getSimpleName(), durationMs);
  }

  private static void testRemoveFromEnd(List<String> list) {
    long start = System.nanoTime();
    for (int i = 0; i < 1000; i++) {
      list.remove(list.size() - 1);
    }
    long end = System.nanoTime();
    long durationMs = (end - start) / 1_000_000;
    printResult("Удаление из конца", list.getClass().getSimpleName(), durationMs);
  }

  private static void printResult(String operation, String type, long timeMs) {
    if (type.equals("ArrayList")) {
      System.out.printf("%-20s | %-15d | ", operation, timeMs);
    } else {
      System.out.printf("%-15d%n", timeMs);
    }
  }
}

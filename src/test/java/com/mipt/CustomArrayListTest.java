package com.mipt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListTest {

  @Test
  void testAddAndGet() {
    CustomArrayList<String> list = new CustomArrayList<>();
    list.add("Hello");
    assertEquals("Hello", list.get(0));
  }

  @Test
  void testSizeAndIsEmpty() {
    CustomArrayList<String> list = new CustomArrayList<>();
    assertTrue(list.isEmpty());
    assertEquals(0, list.size());

    list.add("Item");
    assertFalse(list.isEmpty());
    assertEquals(1, list.size());
  }

  @Test
  void testRemove() {
    CustomArrayList<String> list = new CustomArrayList<>();
    list.add("A");
    list.add("B");
    list.add("C");

    assertEquals("B", list.remove(1));
    assertEquals(2, list.size());
    assertEquals("A", list.get(0));
    assertEquals("C", list.get(1));
  }

  @Test
  void testAddNullThrowsException() {
    CustomArrayList<String> list = new CustomArrayList<>();
    assertThrows(NullPointerException.class, () -> list.add(null));
  }
}
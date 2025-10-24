package com.mipt.hw5;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentMapTest {

  @Test
  public void testAllMetods() {
    // Создаём HashMap
    Map<Integer, Student> hashMap = new HashMap<>();

    // Создаём TreeMap с сортировкой по убыванию id
    TreeMap<Integer, Student> treeMap = new TreeMap<>((id1, id2) -> Integer.compare(id2, id1));

    Student s1 = new Student(1, "Женя", 4.5);
    Student s2 = new Student(2, "Олег", 3.8);
    Student s3 = new Student(3, "Андрей", 4.9);
    Student s4 = new Student(4, "Арина", -999);
    Student s5 = new Student(5, "Влад", 4.0);
    Student s6 = new Student(6, "Миша", 3.9);

    hashMap.put(s1.getId(), s1);
    hashMap.put(s2.getId(), s2);
    hashMap.put(s3.getId(), s3);
    hashMap.put(s4.getId(), s4);
    hashMap.put(s5.getId(), s5);
    hashMap.put(s6.getId(), s6);

    treeMap.put(s1.getId(), s1);
    treeMap.put(s2.getId(), s2);
    treeMap.put(s3.getId(), s3);
    treeMap.put(s4.getId(), s4);
    treeMap.put(s5.getId(), s5);
    treeMap.put(s6.getId(), s6);

    //=== Поиск студентов по диапазону оценок ===
    List<Student> studentsInRange = findStudentsByGradeRange(hashMap, 4.0, 5.0);
    assertEquals(s1, studentsInRange.getFirst());
    assertEquals(s3, studentsInRange.get(1));
    assertEquals(s5, studentsInRange.getLast());

    //=== Топ 2 студента по id (убывание) ===
    List<Student> topStudents = getTopNStudents(treeMap, 2);
    assertEquals(s6, topStudents.getFirst());
    assertEquals(s5, topStudents.getLast());
  }

  public static List<Student> findStudentsByGradeRange(Map<Integer, Student> map, double minGrade, double maxGrade) {
    List<Student> result = new ArrayList<>();
    for (Student student : map.values()) {
      if (student.getGrade() >= minGrade && student.getGrade() <= maxGrade) {
        result.add(student);
      }
    }
    return result;
  }

  public static List<Student> getTopNStudents(TreeMap<Integer, Student> map, int n) {
    List<Student> result = new ArrayList<>();
    int count = 0;
    for (Student student : map.values()) {
      if (count < n) {
        result.add(student);
        count++;
      } else {
        break;
      }
    }
    return result;
  }
}

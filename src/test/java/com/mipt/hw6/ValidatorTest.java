package com.mipt.hw6;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

  static class User {
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    private String name;

    @Email(message = "Invalid email format")
    @NotNull(message = "Email cannot be null")
    private String email;

    @Range(min = 0, max = 150, message = "Age must be between 0 and 150")
    private Integer age;

    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
  }

  @Test
  void testValidObject() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setAge(30);
    user.setPassword("123456789");

    ValidationResult result = Validator.validate(user);
    assertTrue(result.isValid());
    assertEquals(0, result.getErrors().size());
  }

  @Test
  void testNullName() {
    User user = new User();
    user.setName(null);
    user.setEmail("john@example.com");
    user.setAge(30);
    user.setPassword("123456789");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertEquals(1, result.getErrors().size());
    assertTrue(result.getErrors().contains("Name cannot be null"));
  }

  @Test
  void testNameTooShort() {
    User user = new User();
    user.setName("A");
    user.setEmail("john@example.com");
    user.setAge(30);
    user.setPassword("securePass123");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertEquals(1, result.getErrors().size());
    assertTrue(result.getErrors().contains("Name must be between 2 and 30 characters"));
  }

  @Test
  void testNameLength30() {
    User user = new User();
    user.setName("111111111111111111111111111111");
    user.setEmail("john@example.com");
    user.setAge(30);
    user.setPassword("securePass123");

    ValidationResult result = Validator.validate(user);
    assertTrue(result.isValid());
    assertEquals(0, result.getErrors().size());
  }

  @Test
  void testNameLength2() {
    User user = new User();
    user.setName("AD");
    user.setEmail("john@example.com");
    user.setAge(30);
    user.setPassword("securePass123");

    ValidationResult result = Validator.validate(user);
    assertTrue(result.isValid());
    assertEquals(0, result.getErrors().size());
  }

  @Test
  void testNameTooLong() {
    User user = new User();
    user.setName("эвыббвембебеэвымбебебе омуму осас ультракилсупер1337");
    user.setEmail("john@example.com");
    user.setAge(30);
    user.setPassword("123456789");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertEquals(1, result.getErrors().size());
    assertTrue(result.getErrors().contains("Name must be between 2 and 30 characters"));
  }

  @Test
  void testInvalidEmail() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("invalid-email");
    user.setAge(30);
    user.setPassword("123456789");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertEquals(1, result.getErrors().size());
    assertTrue(result.getErrors().contains("Invalid email format"));
  }

  @Test
  void testAgeOutOfRange() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setAge(200);
    user.setPassword("123456789");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertEquals(1, result.getErrors().size());
    assertTrue(result.getErrors().contains("Age must be between 0 and 150"));
  }

  @Test
  void testAge150() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setAge(150);
    user.setPassword("123456789");

    ValidationResult result = Validator.validate(user);
    assertTrue(result.isValid());
    assertEquals(0, result.getErrors().size());
  }

  @Test
  void testAge0() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setAge(0);
    user.setPassword("123456789");

    ValidationResult result = Validator.validate(user);
    assertTrue(result.isValid());
    assertEquals(0, result.getErrors().size());
  }

  @Test
  void testMultipleErrors() {
    User user = new User();
    user.setName(null);
    user.setEmail("invalid-email");
    user.setAge(-5);
    user.setPassword("short");

    ValidationResult result = Validator.validate(user);
    assertFalse(result.isValid());
    assertEquals(4, result.getErrors().size());
    assertTrue(result.getErrors().contains("Name cannot be null"));
    assertTrue(result.getErrors().contains("Invalid email format"));
    assertTrue(result.getErrors().contains("Age must be between 0 and 150"));
    assertTrue(result.getErrors().contains("Password must be between 6 and 20 characters"));
  }

  @Test
  void testNullObject() {
    ValidationResult result = Validator.validate(null);
    assertFalse(result.isValid());
    assertEquals(1, result.getErrors().size());
    assertTrue(result.getErrors().contains("Object is null"));
  }
}

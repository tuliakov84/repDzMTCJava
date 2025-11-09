package com.mipt.hw6;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Validator {

  public static ValidationResult validate(Object object) {
    ValidationResult result = new ValidationResult();

    if (object == null) {
      result.addError("Object is null");
      return result;
    }

    Class<?> clazz = object.getClass();
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);

      try {
        Object value = field.get(object);

        // Проверка @NotNull
        if (field.isAnnotationPresent(NotNull.class)) {
          NotNull notNull = field.getAnnotation(NotNull.class);
          if (value == null) {
            result.addError(notNull.message());
          }
        }

        // Проверка @Size (String)
        if (field.isAnnotationPresent(Size.class)) {
          Size size = field.getAnnotation(Size.class);
          if (value instanceof String) {
            String strValue = (String) value;
            int len = strValue.length();
            if (len < size.min() || len > size.max()) {
              result.addError(size.message());
            }
          } else if (value != null) {
            result.addError("not a String");
          }
        }

        // Проверка @Range
        if (field.isAnnotationPresent(Range.class)) {
          Range range = field.getAnnotation(Range.class);
          if (value instanceof Number) {
            Number number = (Number) value;
            long longValue = number.longValue();
            if (longValue < range.min() || longValue > range.max()) {
              result.addError(range.message());
            }
          } else if (value != null) {
            result.addError("not a Number");          }
        }

        // Проверка @Email (String)
        if (field.isAnnotationPresent(Email.class)) {
          Email email = field.getAnnotation(Email.class);
          if (value instanceof String strValue) {
            if (!isValidEmail(strValue)) {
              result.addError(email.message());
            }
          } else if (value != null) {
            result.addError("not a String");          }
        }

      } catch (IllegalAccessException e) {
        result.addError("Cannot access field: " + field.getName());
      }
    }

    return result;
  }

  private static boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }
    String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
    return Pattern.matches(emailRegex, email);
  }
}

package com.mipt.hw6;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {
  int min() default 0;
  int max();
  String message() default "String length must be between min and max";
}

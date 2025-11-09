package com.mipt.hw6;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Range {
  int min();
  int max();
  String message() default "Number must be in range [min, max]";
}

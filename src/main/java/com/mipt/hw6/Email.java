package com.mipt.hw6;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {
  String message() default "Invalid email format";
}

package com.mipt.hw6;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
  private boolean isValid;
  private ArrayList<String> errors;

  public ValidationResult() {
    this.isValid = true;
    this.errors = new ArrayList<>();
  }

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean valid) {
    isValid = valid;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void addError(String error) {
    this.errors.add(error);
    this.isValid = false;
  }
}

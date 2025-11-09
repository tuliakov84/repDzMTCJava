package com.mipt.hw4;

public class Calculator<T extends Number> {
  public double sum(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() + b.doubleValue();
  }

  public double subtract(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() - b.doubleValue();
  }

  public double multiply(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() * b.doubleValue();
  }

  public double divide(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    if (b.doubleValue() == 0.0) {
      return Double.NaN;
    }
    return a.doubleValue() / b.doubleValue();
  }

  public static void main(String[] args) {
    // пример использования
    final Calculator<Integer> intCalc = new Calculator<>();
    double result = intCalc.sum(5, 3); // 8.0
    System.out.println(result);
    result = intCalc.subtract(5, 3); // 2.0
    System.out.println(result);
    result = intCalc.multiply(5, 3); // 15.0
    System.out.println(result);
    result = intCalc.divide(5, 3); // 1.66666666667
    System.out.println(result);
    result = intCalc.sum(5, null);
    System.out.println(result);
    result = intCalc.subtract(5, null);
    System.out.println(result);
    result = intCalc.multiply(5, null);
    System.out.println(result);
    result = intCalc.divide(5, 0);
    System.out.println(result);
    System.out.println();

    final Calculator<Double> doubleCalc = new Calculator<>();
    double res = doubleCalc.divide(10.0, 4.0); // 2.5
    System.out.println(res);
    res = doubleCalc.multiply(10.0, 4.0); // 40.0
    System.out.println(res);
    res = doubleCalc.subtract(10.0, 4.0); // 6.0
    System.out.println(res);
    res = doubleCalc.divide(10.0, 0.0); // 2.5
    System.out.println(res);
    res = doubleCalc.multiply(10.0, null); // 40.0
    System.out.println(res);
  }
}

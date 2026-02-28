package com.mipt.hw9_multithreading;

import java.util.UUID;

public class BankAccount {
  private int balance;
  private final UUID id;

  public BankAccount(int balance) {
    this.balance = balance;
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }

  public int getBalance() {
    return balance;
  }

  public void withDraw(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Сумма снятия не может быть отрицательной");
    }
    if (amount > balance) {
      throw new IllegalArgumentException("Недостаточно средств на счете " + id);
    }
    balance -= amount;
  }

  public void deposit(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Сумма депозита не может быть отрицательной");
    }
    this.balance += amount;
  }

  @Override
  public String toString() {
    return "Аккаунт #" + id + " [Баланс: " + balance + "]";
  }
}
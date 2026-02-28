package com.mipt.hw9_multithreading;

public class Bank {
  public void sendToAccount(BankAccount source, BankAccount destination, int amount) {
    if (source == null || destination == null) throw new IllegalArgumentException();

    BankAccount firstAccount;
    BankAccount secondAccount;

    if (source.getId().compareTo(destination.getId()) < 0) {
      firstAccount = source;
      secondAccount = destination;
    } else {
      firstAccount = destination;
      secondAccount = source;
    }

    synchronized (firstAccount) {
      synchronized (secondAccount) {
        source.withDraw(amount);
        destination.deposit(amount);
      }
    }
  }

  public void sendToAccountDeadlock(BankAccount source, BankAccount destination, int amount) {
    if (source == null || destination == null) throw new IllegalArgumentException();

    synchronized (source) {
      synchronized (destination) {
        source.withDraw(amount);
        destination.deposit(amount);
      }
    }
  }
}

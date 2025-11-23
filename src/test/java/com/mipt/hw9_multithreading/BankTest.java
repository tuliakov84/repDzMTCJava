package com.mipt.hw9_multithreading;

package com.mipt.hw9_multithreading;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class BankTest {
  @FunctionalInterface
  interface BankTransferFunction {
    void run(BankAccount firstAccount, BankAccount secondAccount, int amount);
  }

  @Test
  public void sendToAccountShouldSendMoneySuccessfully() {
    BankAccount source = new BankAccount(1000);
    BankAccount destination = new BankAccount(0);
    Bank bank = new Bank();

    System.out.println(source.getBalance());

    bank.sendToAccount(source, destination, 400);

    assertEquals(600, source.getBalance());
    assertEquals(400, destination.getBalance());
  }

  @Test
  public void sendToAccountShouldThrowException() {
    BankAccount source = new BankAccount(1000);
    BankAccount destination = new BankAccount(0);
    Bank bank = new Bank();

    assertThrows(
        IllegalArgumentException.class,
        () -> bank.sendToAccount(source, destination, 1200));
  }

  @Test
  public void sendToAccountShouldNotSendMoneyMoneyLessThen0() {
    BankAccount source = new BankAccount(1000);
    BankAccount destination = new BankAccount(0);
    Bank bank = new Bank();

    assertThrows(
        IllegalArgumentException.class,
        () -> bank.sendToAccount(source, destination, -1));
  }

  @Test
  public void sendToAccountShouldThrowExceptionWhenSourceOrDestinationIsNull() {
    BankAccount account = new BankAccount(1000);
    Bank bank = new Bank();

    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(null, account, 1));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(account, null, 1));
    assertThrows(IllegalArgumentException.class, () -> bank.sendToAccount(null, null, 1));
  }

  @Test
  public void sendToAccountWithDeadlock() throws InterruptedException {
    Bank bank = new Bank();
    testDeadlock(true, bank::sendToAccountDeadlock);
  }

  @Test
  public void sendToAccountWithoutDeadlock() throws InterruptedException {
    Bank bank = new Bank();
    testDeadlock(false, bank::sendToAccount);
  }

  private void testDeadlock(boolean shouldBeDeadlock, BankTransferFunction transferFunction)
      throws InterruptedException {
    final int MAX_AWAIT_TIME = 1000;
    final int ITERATIONS_NUM = 1000;
    final int THREADS_NUM = 100;

    AtomicInteger completedCounter = new AtomicInteger(0);

    BankAccount firstAccount = new BankAccount(THREADS_NUM * ITERATIONS_NUM);
    BankAccount secondAccount = new BankAccount(THREADS_NUM * ITERATIONS_NUM);

    List<Thread> threads = new ArrayList<>(THREADS_NUM);

    for (int i = 0; i < THREADS_NUM; i++) {
      Thread thread;

      if (i % 2 == 0) {
        thread = new Thread(
            () -> {
              for (int j = 0; j < ITERATIONS_NUM; ++j) {
                transferFunction.run(firstAccount, secondAccount, 1);
                completedCounter.incrementAndGet();
              }
            });
      } else {
        thread = new Thread(
            () -> {
              for (int j = 0; j < ITERATIONS_NUM; ++j) {
                transferFunction.run(secondAccount, firstAccount, 1);
                completedCounter.incrementAndGet();
              }
            });
      }

      threads.add(thread);
      thread.start();
    }

    Thread.sleep(MAX_AWAIT_TIME);

    for (Thread thread : threads) {
      thread.interrupt();
    }

    assertEquals(shouldBeDeadlock, completedCounter.get() < THREADS_NUM * ITERATIONS_NUM);
  }
}


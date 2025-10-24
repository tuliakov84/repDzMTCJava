package com.mipt.hw5;
import java.util.Iterator;
import java.lang.ArrayIndexOutOfBoundsException;

interface CustomList<A> extends Iterable<A>{
  void add(A elem);

  A get(int ind);

  A remove(int ind);

  boolean isEmpty();

  int size();
}


public class CustomArrayList<A> implements CustomList<A> {
  private static final int DEFAULT_CAPACITY = 10;
  private static final double KOEFF = 1.5;
  private int size;
  private Object[] array;
  private int capacity;

  public CustomArrayList() {
    array = new Object[DEFAULT_CAPACITY];
    size = 0;
    capacity = DEFAULT_CAPACITY;
  }

  @Override
  public void add(A elem) {
    if (elem == null) {
      throw new NullPointerException("elem can't be null");
    }
    if (size >= capacity) {
      capacity *= KOEFF;
      Object[] newArray = new Object[capacity];
      for (int i = 0; i < size; i++) {
        newArray[i] = array[i];
      }
      array = newArray;
    }
    array[size] = elem;
    ++size;
  }

  @Override
  public A get(int index) {
    if (index >= size || index < 0) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return (A) array[index];
  }

  @Override
  public A remove(int ind) {
    if (ind >= size || ind < 0) {
      throw new ArrayIndexOutOfBoundsException();
    }
    A removedElem = (A) array[ind];
    Object[] newArray = new Object[capacity];
    for (int i = 0; i < ind; i++) {
      newArray[i] = array[i];
    }
    for (int i = ind + 1; i < size; i++) {
      newArray[i - 1] = array[i];
    }
    array = newArray;
    array[size - 1] = null;
    --size;
    return removedElem;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public Iterator<A> iterator() {
    return new CustomArrayListIterator();
  }

  private class CustomArrayListIterator implements Iterator<A> {

    private int currentIndex = 0;

    @Override
    public boolean hasNext() {
      return currentIndex < size;
    }

    @Override
    public A next() {
      if (hasNext() == false) {
        throw new ArrayIndexOutOfBoundsException();
      }
      A elem = (A) array[currentIndex];
      currentIndex++;
      return elem;
    }
  }
}

package com.smtp.mock;

import java.util.concurrent.atomic.AtomicInteger;

public class CountCheck {
  private final AtomicInteger count = new AtomicInteger();
  
  public void initialize(int number) {
    count.set(number);
  }
  
  public Integer addOne() {
    return count.incrementAndGet();
  }
  
  public void deleteOne() {
    count.decrementAndGet();
  }
  
  public int getCount() {
    return count.get();
  }
}

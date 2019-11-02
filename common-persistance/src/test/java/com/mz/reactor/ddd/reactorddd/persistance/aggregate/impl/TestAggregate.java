package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

public class TestAggregate {

  private long value;

  public TestAggregate() {
    this(0);
  }

  public TestAggregate(long value) {
    this.value = value;
  }

  public TestAggregateEvent validate(TestAggregateCommand command) {
    return TestAggregateEvent.newBuilder()
        .withAmount(command.getValue())
        .build();
  }

  public TestAggregate apply(TestAggregateEvent event) {
    value = value + event.getAmount();
    return this;
  }

  public long getValue() {
    return value;
  }
}

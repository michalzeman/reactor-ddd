package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.valueobject.Id;

public class TestAggregate {

  private final Id id;

  private long value;

  public TestAggregate(Id id) {
    this(0, id);
  }

  public TestAggregate(long value, Id id) {
    this.value = value;
    this.id = id;
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

  public Id getId() {
    return id;
  }
}

package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.command.Command;

import java.util.Optional;

public class TestAggregateCommand implements Command {

  private final String commandId;

  private final long value;

  private TestAggregateCommand(Builder builder) {
    commandId = builder.commandId;
    value = builder.value;
  }

  public String getCommandId() {
    return commandId;
  }

  public long getValue() {
    return value;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(TestAggregateCommand copy) {
    Builder builder = new Builder();
    builder.commandId = copy.getCommandId();
    builder.value = copy.getValue();
    return builder;
  }

  @Override
  public String commandId() {
    return commandId;
  }

  @Override
  public Optional<String> correlationId() {
    return Optional.empty();
  }

  public static final class Builder {
    private String commandId;
    private long value;

    private Builder() {
    }

    public Builder withCommandId(String val) {
      commandId = val;
      return this;
    }

    public Builder withValue(long val) {
      value = val;
      return this;
    }

    public TestAggregateCommand build() {
      return new TestAggregateCommand(this);
    }
  }
}

package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

public class TestData {

  private final String name;

  private final String signature;

  private final String numbers;

  private TestData(Builder builder) {
    name = builder.name;
    signature = builder.signature;
    numbers = builder.numbers;
  }

  public String getName() {
    return name;
  }

  public String getSignature() {
    return signature;
  }

  public String getNumbers() {
    return numbers;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(TestData copy) {
    Builder builder = new Builder();
    builder.name = copy.getName();
    builder.signature = copy.getSignature();
    builder.numbers = copy.getNumbers();
    return builder;
  }

  public static final class Builder {
    private String name;
    private String signature;
    private String numbers;

    private Builder() {
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withSignature(String signature) {
      this.signature = signature;
      return this;
    }

    public Builder withNumbers(String numbers) {
      this.numbers = numbers;
      return this;
    }

    public TestData build() {
      return new TestData(this);
    }
  }
}

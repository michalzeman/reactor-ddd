package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

public class Person {

  private final String name;

  private final String surname;

  private Person(Builder builder) {
    name = builder.name;
    surname = builder.surname;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(Person copy) {
    Builder builder = new Builder();
    builder.name = copy.getName();
    builder.surname = copy.getSurname();
    return builder;
  }

  public static final class Builder {
    private String name;
    private String surname;

    private Builder() {
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withSurname(String surname) {
      this.surname = surname;
      return this;
    }

    public Person build() {
      return new Person(this);
    }
  }
}

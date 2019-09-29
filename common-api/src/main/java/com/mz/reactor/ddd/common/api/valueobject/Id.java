package com.mz.reactor.ddd.common.api.valueobject;

public class Id extends StringValue {
  public Id(String value) {
    super(value);
  }

  @Override
  public String toString() {
    return "Id{" +
        "value='" + value + '\'' +
        '}';
  }
}

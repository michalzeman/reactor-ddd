package com.mz.reactor.ddd.common.api.valueobject;

import java.util.Objects;
import java.util.Optional;

public class StringValue {
  private final String value;

  public StringValue(String value) {
    this.value = Optional.ofNullable(value)
    .filter(v -> !v.isBlank())
    .orElseThrow(() -> new RuntimeException("Value can't be null or empty"));
  }

  public String getValue() {
    return value;
  }
}

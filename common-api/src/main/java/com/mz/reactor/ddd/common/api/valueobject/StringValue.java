package com.mz.reactor.ddd.common.api.valueobject;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class StringValue {
  private final String value;

  public StringValue(String value) {
    this.value = validateValue.apply(value);
  }

  public String getValue() {
    return value;
  }

  public static Function<String, String> validateValue = value ->
      Optional.ofNullable(value)
          .filter(v -> !v.isBlank())
          .orElseThrow(() -> new RuntimeException("Value can't be null or empty"));

  public static void validate(String... values) {
    Stream.of(values)
        .map(validateValue);
  }
}

package com.mz.reactor.ddd.common.api.valueobject;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class StringValues {

  private StringValues() {
  }

  public static Function<String, String> validateValue = value ->
      Optional.ofNullable(value)
          .filter(v -> !v.isBlank())
          .orElseThrow(() -> new RuntimeException("Value can't be null or empty"));

  public static void validate(String... values) {
    Stream.of(values)
        .forEach(validateValue::apply);
  }
}

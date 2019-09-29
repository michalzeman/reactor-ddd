package com.mz.reactor.ddd.common.api.valueobject;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class StringValue {
  protected final String value;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof StringValue)) return false;
    StringValue that = (StringValue) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "StringValue{" +
        "value='" + value + '\'' +
        '}';
  }
}

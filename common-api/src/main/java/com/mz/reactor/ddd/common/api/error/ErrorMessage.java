package com.mz.reactor.ddd.common.api.error;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableErrorMessage.class)
@JsonDeserialize(as = ImmutableErrorMessage.class)
public interface ErrorMessage {

  String error();

  static ImmutableErrorMessage.Builder builder() {
    return ImmutableErrorMessage.builder();
  }
}

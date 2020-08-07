package com.mz.reactor.ddd.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.Optional;

public interface Message {

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  Optional<String> correlationId();

  @Value.Default
  default Instant createdAt() {
    return Instant.now();
  }
}

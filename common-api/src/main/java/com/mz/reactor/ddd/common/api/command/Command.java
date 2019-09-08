package com.mz.reactor.ddd.common.api.command;

import org.immutables.value.Value;

import java.time.Instant;
import java.util.Optional;

public interface Command {

  Optional<String> correlationId();

  @Value.Default
  default Instant createdAt() {
    return Instant.now();
  }
}

package com.mz.reactor.ddd.common.api.command;

import org.immutables.value.Value;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface Command {

  @Value.Default
  default String commandId() {
    return UUID.randomUUID().toString();
  }

  Optional<String> correlationId();

  @Value.Default
  default Instant createdAt() {
    return Instant.now();
  }
}

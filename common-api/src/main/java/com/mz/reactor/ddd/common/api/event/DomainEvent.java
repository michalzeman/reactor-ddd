package com.mz.reactor.ddd.common.api.event;

import org.immutables.value.Value;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface DomainEvent {

  Optional<String> correlationId();

  @Value.Default
  default String eventId() {
    return UUID.randomUUID().toString();
  }

  @Value.Default
  default Instant createdAt() {
    return Instant.now();
  }


}

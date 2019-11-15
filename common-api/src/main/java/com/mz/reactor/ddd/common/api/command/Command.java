package com.mz.reactor.ddd.common.api.command;

import com.mz.reactor.ddd.common.api.Message;
import org.immutables.value.Value;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface Command extends Message {

  @Value.Default
  default String commandId() {
    return UUID.randomUUID().toString();
  }
}

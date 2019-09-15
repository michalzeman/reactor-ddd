package com.mz.reactor.ddd.common.api.command;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
public interface CommandResult {

  enum StatusCode {
    OK,
    BAD_COMMAND,
    FAILED,
    NOT_MODIFIED;
  }

  StatusCode statusCode();

  List<DomainEvent> events();

  Optional<RuntimeException> error();

  static ImmutableCommandResult.Builder builder() {
    return ImmutableCommandResult.builder();
  }

  static CommandResult fromError(RuntimeException error, DomainEvent event) {
    return builder()
        .statusCode(StatusCode.BAD_COMMAND)
        .events(List.of(event))
        .error(error)
        .build();
  }

  static CommandResult badCommand() {
    return builder()
        .statusCode(StatusCode.BAD_COMMAND)
        .build();
  }

  static CommandResult notModified() {
    return builder()
        .statusCode(StatusCode.NOT_MODIFIED)
        .build();
  }
}

package com.mz.reactor.ddd.common.api.command;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import org.immutables.value.Value;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Value.Immutable
public interface CommandResult {

  enum StatusCode {
    OK,
    BAD_COMMAND,
    FAILED,
    NOT_MODIFIED
  }

  String commandId();

  StatusCode statusCode();

  List<? extends DomainEvent> events();

  Optional<RuntimeException> error();

  static ImmutableCommandResult.Builder builder() {
    return ImmutableCommandResult.builder();
  }

  static CommandResult from(List<? extends DomainEvent> events, Command command) {
    if (Objects.isNull(events) || events.isEmpty()) {
      return notModified(command);
    }

    return Optional.ofNullable(command)
        .map(aCommand -> CommandResult.builder()
            .commandId(aCommand.commandId())
            .events(events)
            .statusCode(StatusCode.OK)
            .build())
        .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified());
  }

  static CommandResult fromError(RuntimeException error, List<? extends DomainEvent> events, Command command) {
    return builder()
        .commandId(Optional.ofNullable(command)
            .map(Command::commandId)
            .orElseGet(() -> UUID.randomUUID().toString()))
        .statusCode(StatusCode.FAILED)
        .events(events)
        .error(error)
        .build();
  }

  static CommandResult badCommand(Command cmd) {
    return builder()
        .commandId(Optional.ofNullable(cmd)
            .map(Command::commandId)
            .orElseGet(() -> UUID.randomUUID().toString()))
        .statusCode(StatusCode.BAD_COMMAND)
        .events(List.of())
        .build();
  }

  static CommandResult notModified(Command cmd) {
    return builder()
        .commandId(Optional.ofNullable(cmd)
            .map(Command::commandId)
            .orElseGet(() -> UUID.randomUUID().toString()))
        .statusCode(StatusCode.NOT_MODIFIED)
        .build();
  }

  static CommandResult notModified() {
    return notModified(null);
  }
}

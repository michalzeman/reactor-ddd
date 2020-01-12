package com.mz.reactor.ddd.common.api.command;

import com.mz.reactor.ddd.common.api.event.DomainEvent;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface CommandHandler<A, C extends Command> {

  CommandResult execute(A aggregate, C command);

  default Function<DomainEvent, ImmutableCommandResult> getCommandResultOk(String commandId) {
    return e -> CommandResult.builder()
        .commandId(commandId)
        .statusCode(CommandResult.StatusCode.OK)
        .events(List.of(e))
        .build();
  }

}

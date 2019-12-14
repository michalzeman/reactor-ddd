package com.mz.reactor.ddd.common.api.command;

import com.mz.reactor.ddd.common.api.event.DomainEvent;

@FunctionalInterface
public interface CommandHandler<A, C extends Command> {

  CommandResult execute(A aggregate, C command);

}

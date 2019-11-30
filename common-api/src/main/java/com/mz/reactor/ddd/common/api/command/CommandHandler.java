package com.mz.reactor.ddd.common.api.command;

import com.mz.reactor.ddd.common.api.event.DomainEvent;

@FunctionalInterface
public interface CommandHandler<A, C extends Command> {

  <E extends DomainEvent> CommandResult<E> execute(A aggregate, C command);

}

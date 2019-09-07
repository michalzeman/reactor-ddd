package com.mz.reactor.ddd.common.api.command;

@FunctionalInterface
public interface CommandHandler<A, C extends Command> {

  CommandResult execute(A aggregate, C command);

}

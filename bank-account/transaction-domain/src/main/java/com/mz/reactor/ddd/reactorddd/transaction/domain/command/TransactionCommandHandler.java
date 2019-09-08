package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class TransactionCommandHandler implements CommandHandler<TransactionAggregate, TransactionCommand> {

  private final Map<Class, BiFunction> handlers = new HashMap<>();

  public TransactionCommandHandler() {
    addHandler(CreateTransaction.class, this::doCreateTransaction);
  }

  private <C extends TransactionCommand> void addHandler(
      Class<C> kClass,
      BiFunction<TransactionAggregate, C, CommandResult> handler
  ) {
    handlers.put(kClass, handler);
  }

  private CommandResult doCreateTransaction(TransactionAggregate aggregate, CreateTransaction command) {
    return (CommandResult) handlers.get(command).apply(aggregate, command);
  }

  @Override
  public CommandResult execute(TransactionAggregate aggregate, TransactionCommand command) {
    return null;
  }
}

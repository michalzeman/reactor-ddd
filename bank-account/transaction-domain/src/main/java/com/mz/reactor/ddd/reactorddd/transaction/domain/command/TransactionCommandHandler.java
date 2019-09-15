package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.command.ImmutableCommandResult;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;

import java.util.List;
import java.util.Optional;

public class TransactionCommandHandler implements CommandHandler<TransactionAggregate, TransactionCommand> {


  public TransactionCommandHandler() {
  }

  private CommandResult doCreateTransaction(TransactionAggregate aggregate, CreateTransaction command) {
    try {
      return Optional.ofNullable(aggregate.validateCreateTransaction(command))
          .map(e -> CommandResult.builder()
              .events(List.of(e))
              .statusCode(CommandResult.StatusCode.OK)
              .build())
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified());
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .events(List.of(TransactionFailed.from(command)))
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .build();
    }
  }

  @Override
  public CommandResult execute(TransactionAggregate aggregate, TransactionCommand command) {
    if (command instanceof CreateTransaction) {
      return doCreateTransaction(aggregate, (CreateTransaction) command);
    } else {
      return CommandResult.badCommand();
    }
  }
}

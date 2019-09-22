package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.command.ImmutableCommandResult;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.FinishTransactionFailed;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;

import java.util.List;
import java.util.Optional;

public class TransactionCommandHandler implements CommandHandler<TransactionAggregate, TransactionCommand> {


  public TransactionCommandHandler() {
  }

  private CommandResult doCreateTransaction(TransactionAggregate aggregate, CreateTransaction command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateCreateTransaction)
          .map(e -> CommandResult.builder()
              .addEvents(e)
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

  private CommandResult doFinishTransaction(TransactionAggregate aggregate, FinishTransaction commad) {
    try {
      return Optional.of(commad)
          .map(aggregate::validateFinishTransaction)
          .map(e -> CommandResult.builder().build())
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified());
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .addEvents(FinishTransactionFailed.builder()
              .aggregateId(commad.aggregateId())
              .correlationId(commad.correlationId())
              .fromAccountId(commad.fromAccountId())
              .toAccountId(commad.toAccountId())
              .build())
          .build();
    }
  }

  @Override
  public CommandResult execute(TransactionAggregate aggregate, TransactionCommand command) {
    if (command instanceof CreateTransaction) {
      return doCreateTransaction(aggregate, (CreateTransaction) command);
    }
    if (command instanceof FinishTransaction) {
      return doFinishTransaction(aggregate, (FinishTransaction) command);
    }else {
      return CommandResult.badCommand();
    }
  }
}

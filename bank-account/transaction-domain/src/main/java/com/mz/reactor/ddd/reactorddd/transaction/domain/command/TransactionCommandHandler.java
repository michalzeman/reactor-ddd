package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.command.ImmutableCommandResult;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.FinishTransactionFailed;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionCommandHandler implements CommandHandler<TransactionAggregate, TransactionCommand> {


  public TransactionCommandHandler() {
  }

  private CommandResult doCreateTransaction(TransactionAggregate aggregate, CreateTransaction command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateCreateTransaction)
          .map(e -> CommandResult.builder()
              .commandId(command.commandId())
              .addEvents(e)
              .statusCode(CommandResult.StatusCode.OK)
              .build())
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .commandId(Optional.ofNullable(command)
              .map(Command::commandId)
              .orElseGet(() -> UUID.randomUUID().toString()))
          .events(List.of(TransactionFailed.from(command)))
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .build();
    }
  }

  private CommandResult doFinishTransaction(TransactionAggregate aggregate, FinishTransaction command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateFinishTransaction)
          .map(e -> CommandResult.builder().build())
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .commandId(Optional.ofNullable(command)
              .map(Command::commandId)
              .orElseGet(() -> UUID.randomUUID().toString()))
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .addEvents(FinishTransactionFailed.builder()
              .aggregateId(command.aggregateId())
              .correlationId(command.correlationId())
              .fromAccountId(command.fromAccountId())
              .toAccountId(command.toAccountId())
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
      return CommandResult.badCommand(command);
    }
  }
}

package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.command.ImmutableCommandResult;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.*;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.FinishTransactionFailed;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionCommandHandler implements CommandHandler<TransactionAggregate, TransactionCommand> {


  public TransactionCommandHandler() {
  }

  @Override
  public CommandResult execute(TransactionAggregate aggregate, TransactionCommand command) {
    if (command instanceof CreateTransaction createTransaction) {
      return doCreateTransaction(aggregate, createTransaction);
    } else if (command instanceof FinishTransaction finishTransaction) {
      return doFinishTransaction(aggregate, finishTransaction);
    } else if (command instanceof CancelTransaction cancelTransaction) {
      return doCancelTransaction(aggregate, cancelTransaction);
    } else if (command instanceof ValidateTransactionMoneyWithdraw validateTransactionMoneyWithdraw) {
      return doValidateTransactionMoneyWithdraw(aggregate, validateTransactionMoneyWithdraw);
    } else if (command instanceof ValidateTransactionMoneyDeposit validateTransactionMoneyDeposit) {
      return doValidateTransactionMoneyDeposit(aggregate, validateTransactionMoneyDeposit);
    } else {
      return CommandResult.badCommand(command);
    }
  }

  private CommandResult doValidateTransactionMoneyDeposit(TransactionAggregate aggregate, ValidateTransactionMoneyDeposit command) {
    return Optional.of(command)
        .map(aggregate::validateTransactionMoneyDeposit)
        .map(e -> CommandResult.builder()
            .commandId(command.commandId())
            .events(e)
            .statusCode(CommandResult.StatusCode.OK)
            .build())
        .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
  }

  private CommandResult doValidateTransactionMoneyWithdraw(TransactionAggregate aggregate, ValidateTransactionMoneyWithdraw command) {
    return Optional.of(command)
        .map(aggregate::validateTransactionMoneyWithdraw)
        .map(e -> CommandResult.builder()
            .commandId(command.commandId())
            .events(e)
            .statusCode(CommandResult.StatusCode.OK)
            .build())
        .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
  }

  private CommandResult doCancelTransaction(TransactionAggregate aggregate, CancelTransaction command) {
    try {
      return CommandResult.from(aggregate.validateCancelTransaction(command), command);
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .commandId(Optional.ofNullable(command)
              .map(Command::commandId)
              .orElseGet(() -> UUID.randomUUID().toString()))
          .events(List.of(TransactionFailed.builder()
              .aggregateId(command.aggregateId())
              .correlationId(command.correlationId())
              .fromAccountId(command.fromAccountId())
              .toAccountId(command.toAccountId())
              .amount(aggregate.getState().amount())
              .build()))
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .build();
    }
  }

  private CommandResult doCreateTransaction(TransactionAggregate aggregate, CreateTransaction command) {
    try {
      return CommandResult.from(List.of(aggregate.validateCreateTransaction(command)), command);
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
      return CommandResult.from(List.of(aggregate.validateFinishTransaction(command)), command);
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .commandId(Optional.ofNullable(command)
              .map(Command::commandId)
              .orElseGet(() -> UUID.randomUUID().toString()))
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .events(List.of(FinishTransactionFailed.builder()
              .aggregateId(command.aggregateId())
              .correlationId(command.correlationId())
              .fromAccountId(command.fromAccountId())
              .toAccountId(command.toAccountId())
              .build()))
          .build();
    }
  }
}

package com.mz.reactor.ddd.reactorddd.account.domain;

import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.command.ImmutableCommandResult;
import com.mz.reactor.ddd.reactorddd.account.domain.command.*;
import com.mz.reactor.ddd.reactorddd.account.domain.event.*;

import java.util.List;
import java.util.Optional;

public class AccountCommandHandler implements CommandHandler<AccountAggregate, AccountCommand> {

  @Override
  public CommandResult execute(AccountAggregate aggregate, AccountCommand command) {
    if (command instanceof WithdrawMoney) {
      return doWithdrawMoney(aggregate, (WithdrawMoney) command);
    } else if (command instanceof CreateAccount) {
      return doCreateAccount(aggregate, (CreateAccount) command);
    } else if (command instanceof DepositMoney) {
      return doDepositMoney(aggregate, (DepositMoney) command);
    } else if (command instanceof WithdrawTransferMoney) {
      return doWithdrawTransferMoney(aggregate, (WithdrawTransferMoney) command);
    } else if (command instanceof DepositTransferMoney) {
      return doDepositTransferMoney(aggregate, (DepositTransferMoney) command);
    } else if (command instanceof FinishOpenedTransaction) {
      return doFinishOpenedTransaction(aggregate, (FinishOpenedTransaction) command);
    } else {
      return CommandResult.badCommand(command);
    }
  }

  private CommandResult doFinishOpenedTransaction(AccountAggregate aggregate, FinishOpenedTransaction command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateFinishOpenedTransaction)
          .map(getCommandResultOk(command.commandId()))
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
    } catch (RuntimeException e) {
      return CommandResult.fromError(e, List.of(), command);
    }
  }

  private CommandResult doWithdrawMoney(AccountAggregate aggregate, WithdrawMoney command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateWithdrawMoney)
          .map(getCommandResultOk(command.commandId()))
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
    } catch (RuntimeException e) {
      return CommandResult.fromError(e, List.of(WithdrawMoneyFailed.builder()
          .aggregateId(command.aggregateId())
          .amount(command.amount())
          .correlationId(command.correlationId())
          .build()), command);
    }
  }

  private CommandResult doCreateAccount(AccountAggregate aggregate, CreateAccount command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateCreateAccount)
          .map(getCommandResultOk(command.commandId()))
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
    } catch (RuntimeException e) {
      return CommandResult.fromError(
          e,
          List.of(CreateAccountFailed.builder()
              .aggregateId(command.aggregateId())
              .balance(command.balance())
              .correlationId(command.correlationId())
              .build()),
          command
      );
    }
  }

  private CommandResult doDepositMoney(AccountAggregate aggregate, DepositMoney command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateDepositMoney)
          .map(getCommandResultOk(command.commandId()))
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
    } catch (RuntimeException e) {
      return CommandResult.fromError(
          e,
          List.of(DepositMoneyFailed.builder()
              .aggregateId(command.aggregateId())
              .amount(command.amount())
              .correlationId(command.correlationId())
              .build()),
          command
      );
    }
  }

  private CommandResult doWithdrawTransferMoney(AccountAggregate aggregate, WithdrawTransferMoney command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateWithdrawTransferMoney)
          .map(getCommandResultOk(command.commandId()))
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
    } catch (RuntimeException e) {
      return CommandResult.fromError(
          e,
          List.of(WithdrawTransferMoneyFailed.builder()
              .transactionId(command.transactionId())
              .aggregateId(command.aggregateId())
              .amount(command.amount())
              .correlationId(command.correlationId())
              .fromAccount(command.fromAccount())
              .toAccount(command.toAccount())
              .build()),
          command
      );
    }
  }

  private CommandResult doDepositTransferMoney(AccountAggregate aggregate, DepositTransferMoney command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateDepositTransferMoney)
          .map(getCommandResultOk(command.commandId()))
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified(command));
    } catch (RuntimeException e) {
      return CommandResult.fromError(
          e,
          List.of(DepositTransferMoneyFailed.builder()
              .transactionId(command.transactionId())
              .aggregateId(command.aggregateId())
              .amount(command.amount())
              .correlationId(command.correlationId())
              .fromAccount(command.fromAccount())
              .toAccount(command.toAccount())
              .build()),
          command
      );
    }
  }
}

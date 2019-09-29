package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.command.ImmutableCommandResult;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.event.CreateAccountFailed;
import com.mz.reactor.ddd.reactorddd.account.domain.event.DepositMoneyFailed;
import com.mz.reactor.ddd.reactorddd.account.domain.event.WithdrawMoneyFailed;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class AccountCommandHandler implements CommandHandler<AccountAggregate, AccountCommand> {

  private final Function<AccountCommand, ImmutableCommandResult> commandNotModified = cmd ->
      (ImmutableCommandResult) CommandResult.notModified(cmd);

  private CommandResult doWithdrawMoney(AccountAggregate aggregate, WithdrawMoney command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateWithdrawMoney)
          .map(e -> CommandResult.builder()
              .commandId(command.commandId())
              .statusCode(CommandResult.StatusCode.OK)
              .addEvents(e)
              .build())
          .orElseGet(() -> commandNotModified.apply(command));
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .commandId(Optional.ofNullable(command)
              .map(Command::commandId)
              .orElseGet(() -> UUID.randomUUID().toString()))
          .addEvents(WithdrawMoneyFailed.builder()
              .aggregateId(command.aggregateId())
              .amount(command.amount())
              .correlationId(command.correlationId())
              .build())
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .build();
    }
  }

  private CommandResult doCreateAccount(AccountAggregate aggregate, CreateAccount command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateCreateAccount)
          .map(e -> CommandResult.builder()
              .commandId(command.commandId())
              .statusCode(CommandResult.StatusCode.OK)
              .addEvents(e)
              .build())
          .orElseGet(() -> commandNotModified.apply(command));
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .commandId(Optional.ofNullable(command)
              .map(Command::commandId)
              .orElseGet(() -> UUID.randomUUID().toString()))
          .addEvents(CreateAccountFailed.builder()
              .aggregateId(command.aggregateId())
              .balance(command.balance())
              .correlationId(command.correlationId())
              .build())
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .build();
    }
  }

  private CommandResult doDepositMoney(AccountAggregate aggregate, DepositMoney command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateDepositMoney)
          .map(e -> CommandResult.builder()
              .commandId(command.commandId())
              .statusCode(CommandResult.StatusCode.OK)
              .addEvents(e)
              .build())
          .orElseGet(() -> commandNotModified.apply(command));
    } catch (RuntimeException e) {
      return CommandResult.builder()
          .commandId(Optional.ofNullable(command)
              .map(Command::commandId)
              .orElseGet(() -> UUID.randomUUID().toString()))
          .addEvents(DepositMoneyFailed.builder()
              .aggregateId(command.aggregateId())
              .amount(command.amount())
              .correlationId(command.correlationId())
              .build())
          .statusCode(CommandResult.StatusCode.FAILED)
          .error(e)
          .build();
    }
  }

  @Override
  public CommandResult execute(AccountAggregate aggregate, AccountCommand command) {
    if (command instanceof WithdrawMoney) {
      return doWithdrawMoney(aggregate, (WithdrawMoney) command);
    } else if (command instanceof CreateAccount) {
      return doCreateAccount(aggregate, (CreateAccount) command);
    } else if (command instanceof DepositMoney) {
      return doDepositMoney(aggregate, (DepositMoney) command);
    } else {
      return CommandResult.badCommand(command);
    }
  }
}

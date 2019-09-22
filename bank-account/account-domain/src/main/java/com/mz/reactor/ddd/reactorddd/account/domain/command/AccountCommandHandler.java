package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.command.ImmutableCommandResult;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.event.DepositMoneyFailed;

import java.util.Optional;

public class AccountCommandHandler implements CommandHandler<AccountAggregate, AccountCommand> {

  private CommandResult doWithdrawMoney(AccountAggregate aggregate, WithdrawMoney command) {
    return null;
  }

  private CommandResult doCreateAccount(AccountAggregate aggregate, CreateAccount command) {
    return null;
  }

  private CommandResult doDepositMoney(AccountAggregate aggregate, DepositMoney command) {
    try {
      return Optional.of(command)
          .map(aggregate::validateDepositMoney)
          .map(e -> CommandResult.builder()
              .statusCode(CommandResult.StatusCode.OK)
              .addEvents(e)
              .build())
          .orElseGet(() -> (ImmutableCommandResult) CommandResult.notModified());
    } catch (RuntimeException e) {
      return CommandResult.builder()
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
      return CommandResult.badCommand();
    }
  }
}

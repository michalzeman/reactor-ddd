package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;

public class AccountCommandHandler implements CommandHandler<AccountAggregate, AccountCommand> {

  private CommandResult doWithdrawMoney(AccountAggregate aggregate, WithdrawMoney command) {
    return null;
  }

  private CommandResult doCreateAccount(AccountAggregate aggregate, CreateAccount command) {
    return null;
  }

  private CommandResult doDepositMoney(AccountAggregate aggregate, DepositMoney command) {
    return null;
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

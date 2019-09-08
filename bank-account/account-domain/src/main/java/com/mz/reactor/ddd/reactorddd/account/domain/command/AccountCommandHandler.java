package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AccountCommandHandler implements CommandHandler<AccountAggregate, AccountCommand> {

  private final Map<Class, BiFunction> handlers = new HashMap<>();

  public AccountCommandHandler() {
    addHandler(CreateAccount.class, this::doCreateAccount);
    addHandler(WithdrawMoney.class, this::doWithdrawMoney);
    addHandler(DepositMoney.class, this::doDepositMoney);
  }

  private <K extends AccountCommand> void addHandler(
      Class<K> kClass,
      BiFunction<AccountAggregate, K, CommandResult> handler
  ) {
    this.handlers.put(kClass, handler);
  }

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
    return (CommandResult) handlers.get(command.getClass()).apply(aggregate, command);
  }
}

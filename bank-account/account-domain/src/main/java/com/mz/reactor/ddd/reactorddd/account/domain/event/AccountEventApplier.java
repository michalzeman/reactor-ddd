package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AccountEventApplier implements EventApplier<AccountAggregate, AccountEvent> {

  private AccountAggregate applyAccountCreated(AccountAggregate aggregate, AccountCreated event) {
    return aggregate;
  }

  private AccountAggregate applyMoneyWithdrawn(AccountAggregate aggregate, MoneyWithdrawn event) {
    return aggregate;
  }

  private AccountAggregate applyMoneyDeposited(AccountAggregate aggregate, MoneyDeposited event) {
    return aggregate;
  }

  @Override
  public AccountAggregate apply(AccountAggregate aggregate, AccountEvent event) {
    if (event instanceof AccountCreated) {
      return applyAccountCreated(aggregate, (AccountCreated) event);
    } else if (event instanceof MoneyWithdrawn) {
      return applyMoneyWithdrawn(aggregate, (MoneyWithdrawn) event);
    } else if (event instanceof MoneyDeposited) {
      return applyMoneyDeposited(aggregate, (MoneyDeposited) event);
    } else {
      return aggregate;
    }
  }
}

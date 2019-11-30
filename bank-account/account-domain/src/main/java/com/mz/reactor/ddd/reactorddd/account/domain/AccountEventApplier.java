package com.mz.reactor.ddd.reactorddd.account.domain;

import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountCreated;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountEvent;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyWithdrawn;

public class AccountEventApplier implements EventApplier<AccountAggregate, AccountEvent> {

  private AccountAggregate applyAccountCreated(AccountAggregate aggregate, AccountCreated event) {
    return aggregate.applyAccountCreated(event);
  }

  private AccountAggregate applyMoneyWithdrawn(AccountAggregate aggregate, MoneyWithdrawn event) {
    return aggregate.applyMoneyWithdrawn(event);
  }

  private AccountAggregate applyMoneyDeposited(AccountAggregate aggregate, MoneyDeposited event) {
    return aggregate.applyMoneyDeposited(event);
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
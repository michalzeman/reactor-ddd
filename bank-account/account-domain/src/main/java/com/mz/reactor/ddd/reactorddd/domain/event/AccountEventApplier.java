package com.mz.reactor.ddd.reactorddd.domain.event;

import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.reactorddd.domain.AccountAggregate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AccountEventApplier implements EventApplier<AccountAggregate, AccountEvent> {

  private final Map<Class, BiFunction> appliers = new HashMap<>();

  public AccountEventApplier() {
    addApplier(AccountCreated.class, this::applyAccountCreated);
    addApplier(MoneyWithdrawn.class, this::applyMoneyWithdrawn);
    addApplier(MoneyDeposited.class, this::applyMoneyDeposited);
  }

  private <E extends AccountEvent> void addApplier(
      Class<E> eClass,
      BiFunction<AccountAggregate, E, AccountAggregate> applier
  ) {
    appliers.put(eClass, applier);
  }

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
    return (AccountAggregate) appliers.get(event.getClass()).apply(aggregate, event);
  }
}

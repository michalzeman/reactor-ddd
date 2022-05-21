package com.mz.reactor.ddd.reactorddd.account.domain;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventHandler;
import com.mz.reactor.ddd.reactorddd.account.domain.event.*;

public class AccountEventHandler implements EventHandler<AccountAggregate> {

  @Override
  public <E extends DomainEvent> AccountAggregate apply(AccountAggregate aggregate, E event) {
    if (event instanceof AccountCreated accountCreated) {
      return aggregate.applyAccountCreated(accountCreated);
    } else if (event instanceof MoneyWithdrawn moneyWithdrawn) {
      return aggregate.applyMoneyWithdrawn(moneyWithdrawn);
    } else if (event instanceof MoneyDeposited moneyDeposited) {
      return aggregate.applyMoneyDeposited(moneyDeposited);
    } else if (event instanceof TransferMoneyWithdrawn transferMoneyWithdrawn) {
      return aggregate.applyTransferMoneyWithdrawn(transferMoneyWithdrawn);
    } else if (event instanceof TransferMoneyDeposited transferMoneyDeposited) {
      return aggregate.applyTransferMoneyDeposited(transferMoneyDeposited);
    } else if (event instanceof OpenedTransactionFinished openedTransactionFinished) {
      return aggregate.applyOpenedTransactionFinished(openedTransactionFinished);
    } else {
      return aggregate;
    }
  }
}

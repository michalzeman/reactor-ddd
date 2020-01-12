package com.mz.reactor.ddd.reactorddd.account.domain;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventHandler;
import com.mz.reactor.ddd.reactorddd.account.domain.event.*;

public class AccountEventHandler implements EventHandler<AccountAggregate> {

  @Override
  public <E extends DomainEvent> AccountAggregate apply(AccountAggregate aggregate, E event) {
    if (event instanceof AccountCreated) {
      return applyAccountCreated(aggregate, (AccountCreated) event);
    } else if (event instanceof MoneyWithdrawn) {
      return applyMoneyWithdrawn(aggregate, (MoneyWithdrawn) event);
    } else if (event instanceof MoneyDeposited) {
      return applyMoneyDeposited(aggregate, (MoneyDeposited) event);
    } else if (event instanceof TransferMoneyWithdrawn) {
      return applyTransferMoneyWithdrawn(aggregate, (TransferMoneyWithdrawn) event);
    } else if (event instanceof TransferMoneyDeposited) {
      return applyTransferMoneyDeposited(aggregate, (TransferMoneyDeposited) event);
    } else if (event instanceof OpenedTransactionFinished) {
      return applyOpenedTransactionFinished(aggregate, (OpenedTransactionFinished) event);
    } else {
      return aggregate;
    }
  }

  private AccountAggregate applyOpenedTransactionFinished(AccountAggregate aggregate, OpenedTransactionFinished event) {
    return aggregate.applyOpenedTransactionFinished(event);
  }

  private AccountAggregate applyTransferMoneyDeposited(AccountAggregate aggregate, TransferMoneyDeposited event) {
    return aggregate.applyTransferMoneyDeposited(event);
  }

  private AccountAggregate applyAccountCreated(AccountAggregate aggregate, AccountCreated event) {
    return aggregate.applyAccountCreated(event);
  }

  private AccountAggregate applyMoneyWithdrawn(AccountAggregate aggregate, MoneyWithdrawn event) {
    return aggregate.applyMoneyWithdrawn(event);
  }

  private AccountAggregate applyMoneyDeposited(AccountAggregate aggregate, MoneyDeposited event) {
    return aggregate.applyMoneyDeposited(event);
  }

  private AccountAggregate applyTransferMoneyWithdrawn(AccountAggregate aggregate, TransferMoneyWithdrawn event) {
    return aggregate.applyTransferMoneyWithdrawn(event);
  }
}

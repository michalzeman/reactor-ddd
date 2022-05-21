package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventHandler;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.*;

public class TransactionEventHandler implements EventHandler<TransactionAggregate> {


  @Override
  public <E extends DomainEvent> TransactionAggregate apply(TransactionAggregate aggregate, E event) {
    if (event instanceof TransactionCreated transactionCreated) {
      return aggregate.applyTransactionCreated(transactionCreated);
    } else if (event instanceof TransactionFinished) {
      return aggregate.applyTransactionFinished();
    } else if (event instanceof TransactionFailed) {
      return aggregate.applyTransactionFailed();
    } else if (event instanceof TransactionMoneyWithdrawn) {
      return aggregate.applyTransactionMoneyWithdrawn();
    } else if (event instanceof TransactionMoneyDeposited) {
      return aggregate.applyTransactionMoneyDeposited();
    } else {
      return aggregate;
    }
  }
}

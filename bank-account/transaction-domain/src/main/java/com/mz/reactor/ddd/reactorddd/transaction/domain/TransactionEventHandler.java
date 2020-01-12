package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventHandler;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.*;

public class TransactionEventHandler implements EventHandler<TransactionAggregate> {


  @Override
  public <E extends DomainEvent> TransactionAggregate apply(TransactionAggregate aggregate, E event) {
    if (event instanceof TransactionCreated) {
      return applyTransactionCreated(aggregate, (TransactionCreated) event);
    } else if (event instanceof TransactionFinished) {
      return applyTransactionFinished(aggregate);
    } else if (event instanceof TransactionFailed) {
      return applyTransactionFailed(aggregate);
    } else if (event instanceof TransactionMoneyWithdrawn) {
      return applyTransactionMoneyWithdrawn(aggregate);
    } else if (event instanceof TransactionMoneyDeposited) {
      return applyTransactionMoneyDeposited(aggregate);
    } else {
      return aggregate;
    }
  }

  private TransactionAggregate applyTransactionMoneyDeposited(TransactionAggregate aggregate) {
    return aggregate.applyTransactionMoneyDeposited();
  }

  private TransactionAggregate applyTransactionMoneyWithdrawn(TransactionAggregate aggregate) {
    return aggregate.applyTransactionMoneyWithdrawn();
  }

  private TransactionAggregate applyTransactionFailed(TransactionAggregate aggregate) {
    return aggregate.applyTransactionFailed();
  }

  private TransactionAggregate applyTransactionFinished(TransactionAggregate aggregate) {
    return aggregate.applyTransactionFinished();
  }

  private TransactionAggregate applyTransactionCreated(TransactionAggregate aggregate, TransactionCreated event) {
    return aggregate.applyTransactionCreated(event);
  }


}

package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;

public class TransactionEventApplier implements EventApplier<TransactionAggregate, TransactionEvent> {


  @Override
  public TransactionAggregate apply(TransactionAggregate aggregate, TransactionEvent event) {
    if (event instanceof TransactionCreated) {
      return applyTransactionCreated(aggregate, (TransactionCreated) event);
    } else {
      return aggregate;
    }
  }

  private TransactionAggregate applyTransactionCreated(TransactionAggregate aggregate, TransactionCreated event) {
    return aggregate.applyTransactionCreated(event);
  }
}

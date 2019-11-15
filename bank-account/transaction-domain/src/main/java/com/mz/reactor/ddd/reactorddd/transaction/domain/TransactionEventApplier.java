package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionEvent;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFinished;

public class TransactionEventApplier implements EventApplier<TransactionAggregate, TransactionEvent> {


  @Override
  public TransactionAggregate apply(TransactionAggregate aggregate, TransactionEvent event) {
    if (event instanceof TransactionCreated) {
      return applyTransactionCreated(aggregate, (TransactionCreated) event);
    }
    if (event instanceof TransactionFinished) {
      return applyTransactionFinished(aggregate, (TransactionFinished) event);
    }
    if (event instanceof TransactionFailed) {
      return applyTransactionFailed(aggregate, (TransactionFailed) event);
    } else {
      return aggregate;
    }
  }

  private TransactionAggregate applyTransactionFailed(TransactionAggregate aggregate, TransactionFailed event) {
    return aggregate.applyTransactionFailed(event);
  }

  private TransactionAggregate applyTransactionFinished(TransactionAggregate aggregate, TransactionFinished event) {
    return aggregate.applyTransactionFinished(event);
  }

  private TransactionAggregate applyTransactionCreated(TransactionAggregate aggregate, TransactionCreated event) {
    return aggregate.applyTransactionCreated(event);
  }


}

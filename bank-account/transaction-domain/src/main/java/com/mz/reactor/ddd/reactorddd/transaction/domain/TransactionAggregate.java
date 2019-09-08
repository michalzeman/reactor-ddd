package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.common.api.valueobject.Id;

import java.math.BigDecimal;

public class TransactionAggregate {

  enum TransactionState {
    Created,
    Finished,
    Failed
  }

  private Id aggregateId;

  private Id fromAccount;

  private Id toAccount;

  private BigDecimal amount;

  public TransactionAggregate(String aggregateId) {
    this.aggregateId = new Id(aggregateId);
  }
}

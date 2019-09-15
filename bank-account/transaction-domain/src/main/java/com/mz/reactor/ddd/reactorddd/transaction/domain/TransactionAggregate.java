package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.FinishTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFinished;

import java.math.BigDecimal;

public class TransactionAggregate {

  enum State {
    INITIALIZED,
    CREATED,
    FINISHED,
    FAILED
  }

  private Id aggregateId;

  private Id fromAccount;

  private Id toAccount;

  private BigDecimal amount;

  private State state;

  public TransactionAggregate(String aggregateId) {
    this.aggregateId = new Id(aggregateId);
    state = State.INITIALIZED;
  }

  public TransactionCreated validateCreateTransaction(CreateTransaction command) {
    if (state == State.INITIALIZED) {
      Id.validate(command.fromAccountId(), command.toAccountId());
      if (command.amount().compareTo(BigDecimal.ZERO) <= 0) {
        throw new RuntimeException(String.format("Amount can't be %s", command.amount()));
      }

      return TransactionCreated.builder()
          .aggregateId(this.aggregateId.getValue())
          .correlationId(command.correlationId())
          .amount(command.amount())
          .fromAccountId(command.fromAccountId())
          .toAccountId(command.toAccountId())
          .build();
    } else {
      throw new RuntimeException(String.format("Can't be applied command %s, aggregate is in state %s",
          command, this.state));
    }
  }

  public TransactionFinished validateFinishTransaction(FinishTransaction command) {
    if (state == State.CREATED) {
      return TransactionFinished.builder()
          .aggregateId(aggregateId.getValue())
          .correlationId(command.correlationId())
          .fromAccountId(fromAccount.getValue())
          .toAccountId(toAccount.getValue())
          .build();
    } else {
      throw new RuntimeException(String.format("Transaction in the state: %s can't be finished!", state));
    }
  }

  public TransactionAggregate applyTransactionCreated(TransactionCreated created) {
    this.fromAccount = new Id(created.fromAccountId());
    this.toAccount = new Id(created.toAccountId());
    this.amount = created.amount();
    this.state = State.CREATED;
    return this;
  }

  public TransactionState getState() {
    return TransactionState.builder()
        .amount(amount)
        .fromAccountId(fromAccount.getValue())
        .toAccountId(toAccount.getValue())
        .aggregateId(aggregateId.getValue())
        .build();
  }
}

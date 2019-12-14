package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CancelTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.FinishTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFinished;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class TransactionAggregate {

  private Id aggregateId;

  private Id fromAccount;

  private Id toAccount;

  private BigDecimal amount;

  private TransactionStatus status;

  public TransactionAggregate(String aggregateId) {
    this.aggregateId = new Id(aggregateId);
    status = TransactionStatus.INITIALIZED;
  }

  public TransactionCreated validateCreateTransaction(CreateTransaction command) {
    if (status == TransactionStatus.INITIALIZED) {
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
      throw new RuntimeException(String.format("Can't be applied command %s, aggregate is in TransactionStatus %s",
          command, this.status));
    }
  }

  public TransactionFinished validateFinishTransaction(FinishTransaction command) {
    if (status == TransactionStatus.CREATED) {
      return TransactionFinished.builder()
          .aggregateId(aggregateId.getValue())
          .correlationId(command.correlationId())
          .fromAccountId(fromAccount.getValue())
          .toAccountId(toAccount.getValue())
          .build();
    } else {
      throw new RuntimeException(String.format("Transaction in the state: %s can't be finished!", status));
    }
  }

  public TransactionFailed validateCancelTransaction(CancelTransaction command) {
    if (status == TransactionStatus.CREATED) {
      return TransactionFailed.builder()
          .aggregateId(aggregateId.getValue())
          .correlationId(command.correlationId())
          .fromAccountId(fromAccount.getValue())
          .toAccountId(toAccount.getValue())
          .amount(this.amount)
          .build();
    } else {
      throw new RuntimeException(String.format("Transaction in the state: %s can't be canceled!", status));
    }
  }

  public TransactionAggregate applyTransactionCreated(TransactionCreated created) {
    this.fromAccount = new Id(created.fromAccountId());
    this.toAccount = new Id(created.toAccountId());
    this.amount = created.amount();
    this.status = TransactionStatus.CREATED;
    return this;
  }

  public TransactionAggregate applyTransactionFinished(TransactionFinished event) {
    this.status = TransactionStatus.FINISHED;
    return this;
  }

  public TransactionAggregate applyTransactionFailed(TransactionFailed event) {
    this.status = TransactionStatus.FAILED;
    return this;
  }

  public TransactionState getState() {
    return TransactionState.builder()
        .amount(amount)
        .fromAccountId(fromAccount.getValue())
        .toAccountId(toAccount.getValue())
        .aggregateId(aggregateId.getValue())
        .status(this.status)
        .build();
  }
}

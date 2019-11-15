package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionEventApplier;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

class TransactionEventApplierTest {

  TransactionEventApplier subject = new TransactionEventApplier();

  @Test
  void apply_TransactionCreated() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();
    var transactionCreated = TransactionCreated.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .toAccountId(toAccountId)
        .fromAccountId(fromAccountId)
        .amount(BigDecimal.TEN)
        .build();
    var aggregate = new TransactionAggregate(aggregateId);

    //when
    var status = subject.apply(aggregate, transactionCreated).getStatus();

    //then
    Assertions.assertEquals(status.amount(), BigDecimal.TEN);
    Assertions.assertEquals(status.status(), TransactionStatus.CREATED);
  }

  @Test
  void apply_TransactionFinished() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();
    var transactionCreated = TransactionCreated.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .toAccountId(toAccountId)
        .fromAccountId(fromAccountId)
        .amount(BigDecimal.TEN)
        .build();
    var transactionFinished = TransactionFinished.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .fromAccountId(fromAccountId)
        .toAccountId(toAccountId)
        .build();
    var aggregate = new TransactionAggregate(aggregateId);
    subject.apply(aggregate, transactionCreated);

    //when
    var status = subject.apply(aggregate, transactionFinished).getStatus();

    //then
    Assertions.assertEquals(status.amount(), BigDecimal.TEN);
    Assertions.assertEquals(status.status(), TransactionStatus.FINISHED);
  }

  @Test
  void apply_TransactionFailed() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();
    var transactionCreated = TransactionCreated.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .toAccountId(toAccountId)
        .fromAccountId(fromAccountId)
        .amount(BigDecimal.TEN)
        .build();
    var transactionFailed = TransactionFailed.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .fromAccountId(fromAccountId)
        .toAccountId(toAccountId)
        .amount(BigDecimal.TEN)
        .build();
    var aggregate = new TransactionAggregate(aggregateId);
    subject.apply(aggregate, transactionCreated);

    //when
    var status = subject.apply(aggregate, transactionFailed).getStatus();

    //then
    Assertions.assertEquals(status.amount(), BigDecimal.TEN);
    Assertions.assertEquals(status.status(), TransactionStatus.FAILED);
  }
}
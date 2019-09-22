package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.FinishTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFinished;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAggregateTest {

  @Test
  void validateCreateTransaction_TransactionCreatedTest() {
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();
    var command = CreateTransaction.builder()
        .aggregateId(aggregateId)
        .amount(BigDecimal.ONE)
        .correlationId(correlationId)
        .fromAccountId(fromAccountId)
        .toAccountId(toAccountId)
        .build();
    var aggregate = new TransactionAggregate(aggregateId);
    var result = aggregate.validateCreateTransaction(command);
    assertTrue(result instanceof TransactionCreated);
    assertEquals(result.correlationId().get(), correlationId);
    assertEquals(result.aggregateId(), aggregateId);
    assertEquals(result.toAccountId(), toAccountId);
    assertEquals(result.fromAccountId(), fromAccountId);
  }

  @Test
  void validateCreateTransaction_TransactionFailed_amount() {
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();
    var command = CreateTransaction.builder()
        .aggregateId(aggregateId)
        .amount(BigDecimal.ZERO)
        .correlationId(correlationId)
        .fromAccountId(fromAccountId)
        .toAccountId(toAccountId)
        .build();
    var aggregate = new TransactionAggregate(aggregateId);
    assertThrows(RuntimeException.class, () -> aggregate.validateCreateTransaction(command));
  }

  @Test
  void applyTransactionCreated() {
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();

    var event = TransactionCreated.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.TEN)
        .fromAccountId(fromAccountId)
        .toAccountId(toAccountId)
        .build();

    var aggregate = new TransactionAggregate(aggregateId);
    var state = aggregate.applyTransactionCreated(event).getState();

    assertEquals(state.aggregateId(), aggregateId);
    assertEquals(state.toAccountId(), toAccountId);
    assertEquals(state.fromAccountId(), fromAccountId);
    assertEquals(state.amount(), BigDecimal.TEN);
  }

  @Test
  void validateFinishTransaction_OK() {
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();

    var event = TransactionCreated.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.TEN)
        .fromAccountId(fromAccountId)
        .toAccountId(toAccountId)
        .build();

    var aggregate = new TransactionAggregate(aggregateId);
    aggregate.applyTransactionCreated(event);

    var result = aggregate.validateFinishTransaction(FinishTransaction.builder()
        .toAccountId(toAccountId)
        .fromAccountId(fromAccountId)
        .correlationId(correlationId)
        .aggregateId(aggregateId)
        .build());

    assertTrue(result instanceof TransactionFinished);
    assertEquals(result.correlationId().get(), correlationId);
    assertEquals(result.aggregateId(), aggregateId);
  }

  @Test
  void validateFinishTransaction_FAILED() {
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();

    var aggregate = new TransactionAggregate(aggregateId);

    assertThrows(RuntimeException.class, () -> aggregate.validateFinishTransaction(FinishTransaction.builder()
        .toAccountId(toAccountId)
        .fromAccountId(fromAccountId)
        .correlationId(correlationId)
        .aggregateId(aggregateId)
        .build()));
  }

  @Test
  void applyTransactionFinished() {
  }

  @Test
  void applyTransactionFailed() {
  }
}
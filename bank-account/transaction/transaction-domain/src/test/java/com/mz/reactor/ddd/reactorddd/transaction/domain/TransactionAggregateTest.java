package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.FinishTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.ValidateTransactionMoneyDeposit;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.ValidateTransactionMoneyWithdraw;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFinished;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
    //given
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

    //when
    TransactionFinished.builder()
        .toAccountId(toAccountId)
        .fromAccountId(fromAccountId)
        .aggregateId(aggregateId)
        .build();
    var state = aggregate.applyTransactionFinished().getState();

    //then
    assertEquals(state.status(), TransactionStatus.FINISHED);
  }

  @Test
  void applyTransactionFailed() {
    //given
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

    //when
    TransactionFailed.builder()
        .toAccountId(toAccountId)
        .fromAccountId(fromAccountId)
        .aggregateId(aggregateId)
        .amount(BigDecimal.TEN)
        .build();
    var state = aggregate.applyTransactionFailed().getState();

    //then
    assertEquals(state.status(), TransactionStatus.FAILED);
    assertEquals(state.amount(), BigDecimal.TEN);
  }

  @Test
  void testValidateTransactionMoneyDeposit() {
    //given
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

    var transactionMoneyDeposit = ValidateTransactionMoneyDeposit.builder()
        .accountId(aggregateId)
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .build();

    //when
    aggregate.validateTransactionMoneyDeposit(transactionMoneyDeposit);
    aggregate.applyTransactionMoneyDeposited();

    //then
    var state = aggregate.getState();
    assertThat(state.moneyDeposited()).isTrue();
  }

  @Test
  void testValidateTransactionMoneyWithdraw() {
    //given
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

    var transactionMoneyWithdraw = ValidateTransactionMoneyWithdraw.builder()
        .accountId(aggregateId)
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .build();

    //when
    aggregate.validateTransactionMoneyWithdraw(transactionMoneyWithdraw);
    aggregate.applyTransactionMoneyWithdrawn();

    //then
    var state = aggregate.getState();
    assertThat(state.moneyWithdrawn()).isTrue();
  }
}
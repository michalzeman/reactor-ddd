package com.mz.reactor.ddd.reactorddd.account.domain;

import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import com.mz.reactor.ddd.reactorddd.account.domain.command.DepositMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawTransferMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountCreated;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyWithdrawn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

class AccountAggregateTest {

  @Test
  void validateCreateAccount_Ok() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var aggregate = new AccountAggregate(aggregateId);
    var command = CreateAccount.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .balance(BigDecimal.TEN)
        .build();

    //when
    var event = aggregate.validateCreateAccount(command);
    var state = aggregate.applyAccountCreated(event).getState();

    //then
    Assertions.assertNotNull(event);
    Assertions.assertEquals(event.correlationId().get(), command.correlationId().get());
    Assertions.assertEquals(event.aggregateId(), command.aggregateId());
    Assertions.assertEquals(event.balance().compareTo(BigDecimal.TEN), 0);
    Assertions.assertEquals(state.amount(), BigDecimal.TEN);
  }

  @Test
  void validateDepositMoney_MoneyDeposited() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var aggregate = new AccountAggregate(aggregateId);
    var depositMoney = DepositMoney.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.TEN)
        .build();

    //when
    var event = aggregate.validateDepositMoney(depositMoney);

    //then
    Assertions.assertNotNull(event);
    Assertions.assertEquals(event.correlationId().get(), depositMoney.correlationId().get());
    Assertions.assertEquals(event.aggregateId(), depositMoney.aggregateId());
    Assertions.assertEquals(event.amount().compareTo(BigDecimal.TEN), 0);
  }

  @Test
  void validateDepositMoney_Failed() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var aggregate = new AccountAggregate(aggregateId);
    var depositMoney = DepositMoney.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.ZERO)
        .build();

    //then
    Assertions.assertThrows(RuntimeException.class, () -> aggregate.validateDepositMoney(depositMoney));
  }

  @Test
  void applyMoneyDeposited() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var aggregate = new AccountAggregate(aggregateId);
    var event = MoneyDeposited.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.TEN)
        .build();
    Assertions.assertEquals(aggregate.getState().amount(), BigDecimal.ZERO);

    //when
    var state = aggregate.applyMoneyDeposited(event).getState();

    //then
    Assertions.assertEquals(state.aggregateId(), aggregateId);
    Assertions.assertEquals(state.amount(), event.amount());
  }

  @Test
  void validateWithdrawMoney_MoneyWithdrawn() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var aggregate = new AccountAggregate(aggregateId);
    var accountCreated = AccountCreated.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .balance(BigDecimal.TEN)
        .build();

    aggregate.applyAccountCreated(accountCreated).getState();

    var withdrawMoney = WithdrawMoney.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.valueOf(5))
        .build();

    //when
    var event = aggregate.validateWithdrawMoney(withdrawMoney);
    var state = aggregate.applyMoneyWithdrawn(event).getState();

    //then
    Assertions.assertNotNull(event);
    Assertions.assertEquals(event.correlationId().get(), correlationId);
    Assertions.assertEquals(event.aggregateId(), aggregateId);
    Assertions.assertTrue(event instanceof MoneyWithdrawn);
    Assertions.assertEquals(state.amount(), BigDecimal.valueOf(5));
  }

  @Test
  void validateWithdrawMoney_Failed() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var aggregate = new AccountAggregate(aggregateId);
    var accountCreated = AccountCreated.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .balance(BigDecimal.TEN)
        .build();

    aggregate.applyAccountCreated(accountCreated);

    //when
    var withdrawMoney = WithdrawMoney.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.valueOf(15))
        .build();

    //then
    Assertions.assertThrows(RuntimeException.class, () -> aggregate.validateWithdrawMoney(withdrawMoney));
  }

  @Test
  void applyMoneyTransferred() {
    //given
    var toAccountId = UUID.randomUUID().toString();
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var transactionId = UUID.randomUUID().toString();
    var aggregate = new AccountAggregate(aggregateId);
    var accountCreated = AccountCreated.builder()
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .balance(BigDecimal.TEN)
        .build();

    aggregate.applyAccountCreated(accountCreated).getState();

    var command = WithdrawTransferMoney.builder()
        .transactionId(transactionId)
        .aggregateId(aggregateId)
        .fromAccount(aggregateId)
        .toAccount(toAccountId)
        .amount(BigDecimal.TEN)
        .build();
    Assertions.assertEquals(aggregate.getState().amount(), BigDecimal.TEN);

    //when
    var event = aggregate.validateWithdrawTransferMoney(command);
    var state = aggregate.applyTransferMoneyWithdrawn(event).getState();

    //then
    Assertions.assertEquals(state.aggregateId(), aggregateId);
    Assertions.assertEquals(state.amount(), BigDecimal.ZERO);
  }
}
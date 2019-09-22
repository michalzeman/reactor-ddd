package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountEventApplierTest {

  AccountEventApplier applier = new AccountEventApplier();

  @Test
  void applyAccountCreated() {
    //give
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var accountAggregate = new AccountAggregate(aggregateId);
    var event = AccountCreated.builder()
        .correlationId(correlationId)
        .aggregateId(aggregateId)
        .balance(BigDecimal.TEN)
        .build();
    assertEquals(accountAggregate.getState().amount().compareTo(BigDecimal.ZERO), 0);

    //when
    var result = applier.apply(accountAggregate, event).getState();

    //then
    assertEquals(result.amount().compareTo(BigDecimal.TEN), 0);
    assertEquals(result.aggregateId(), aggregateId);
  }

  @Test
  void applyMoneyDeposited() {
    //give
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var accountAggregate = new AccountAggregate(aggregateId);
    var event = MoneyDeposited.builder()
        .correlationId(correlationId)
        .aggregateId(aggregateId)
        .amount(BigDecimal.TEN)
        .build();
    assertEquals(accountAggregate.getState().amount().compareTo(BigDecimal.ZERO), 0);

    //when
    var result = applier.apply(accountAggregate, event).getState();

    //then
    assertEquals(result.amount().compareTo(BigDecimal.TEN), 0);
    assertEquals(result.aggregateId(), aggregateId);
  }

}
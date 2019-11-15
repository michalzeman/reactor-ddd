package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawMoney;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface MoneyWithdrawn extends AccountEvent {
  BigDecimal amount();

  static ImmutableMoneyWithdrawn.Builder builder() {
    return ImmutableMoneyWithdrawn.builder();
  }

  static MoneyWithdrawn from(WithdrawMoney command) {
    return builder()
        .aggregateId(command.aggregateId())
        .correlationId(command.correlationId())
        .amount(command.amount())
        .build();
  }
}

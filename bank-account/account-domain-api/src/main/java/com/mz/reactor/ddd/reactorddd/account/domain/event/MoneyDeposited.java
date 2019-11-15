package com.mz.reactor.ddd.reactorddd.account.domain.event;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface MoneyDeposited extends AccountEvent {
  BigDecimal amount();

  static ImmutableMoneyDeposited.Builder builder() {
    return ImmutableMoneyDeposited.builder();
  }
}

package com.mz.reactor.ddd.reactorddd.account.domain.event;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface MoneyWithdrawn extends AccountEvent {
  BigDecimal amount();

  static ImmutableMoneyWithdrawn.Builder builder() {
    return ImmutableMoneyWithdrawn.builder();
  }
}

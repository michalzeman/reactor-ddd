package com.mz.reactor.ddd.reactorddd.account.domain.event;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface DepositMoneyFailed extends AccountEvent {

  BigDecimal amount();

  static ImmutableDepositMoneyFailed.Builder builder() {
    return ImmutableDepositMoneyFailed.builder();
  }

}

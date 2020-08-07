package com.mz.reactor.ddd.reactorddd.account.domain.event;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface WithdrawMoneyFailed extends AccountEvent {

  BigDecimal amount();

  static ImmutableWithdrawMoneyFailed.Builder builder() {
    return ImmutableWithdrawMoneyFailed.builder();
  }

}

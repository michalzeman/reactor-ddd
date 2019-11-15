package com.mz.reactor.ddd.reactorddd.account.domain.command;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface WithdrawMoney extends AccountCommand {

  BigDecimal amount();

  static ImmutableWithdrawMoney.Builder builder() {
    return ImmutableWithdrawMoney.builder();
  }
}

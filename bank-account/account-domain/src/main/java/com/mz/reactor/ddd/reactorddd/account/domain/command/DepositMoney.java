package com.mz.reactor.ddd.reactorddd.account.domain.command;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface DepositMoney extends AccountCommand {
  BigDecimal amount();

  static ImmutableDepositMoney.Builder builder() {
    return ImmutableDepositMoney.builder();
  }
}

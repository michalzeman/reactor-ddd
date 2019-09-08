package com.mz.reactor.ddd.reactorddd.account.domain;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface AccountState {
  String aggregateId();

  BigDecimal amount();

  static ImmutableAccountState.Builder builder() {
    return ImmutableAccountState.builder();
  }
}

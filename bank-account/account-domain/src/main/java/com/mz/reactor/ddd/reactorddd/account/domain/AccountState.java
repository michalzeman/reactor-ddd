package com.mz.reactor.ddd.reactorddd.account.domain;

import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.List;

@Value.Immutable
public interface AccountState {
  String aggregateId();

  BigDecimal amount();

  List<String> openedTransactions();

  static ImmutableAccountState.Builder builder() {
    return ImmutableAccountState.builder();
  }
}

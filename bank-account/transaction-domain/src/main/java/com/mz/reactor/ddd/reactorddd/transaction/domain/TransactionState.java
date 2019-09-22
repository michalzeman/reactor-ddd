package com.mz.reactor.ddd.reactorddd.transaction.domain;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface TransactionState {

  String aggregateId();

  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

  TransactionStatus status();

  static ImmutableTransactionState.Builder builder() {
    return ImmutableTransactionState.builder();
  }

}

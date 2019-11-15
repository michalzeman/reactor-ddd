package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface TransactionCreated extends TransactionEvent {
  
  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

  static ImmutableTransactionCreated.Builder builder() {
    return ImmutableTransactionCreated.builder();
  }
}

package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import org.immutables.value.Value;

@Value.Immutable
public interface TransactionFinished extends TransactionEvent {

  String fromAccountId();

  String toAccountId();

  static ImmutableTransactionFinished.Builder builder() {
    return ImmutableTransactionFinished.builder();
  }
}

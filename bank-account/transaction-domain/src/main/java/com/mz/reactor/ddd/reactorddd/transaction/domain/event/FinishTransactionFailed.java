package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import org.immutables.value.Value;

@Value.Immutable
public interface FinishTransactionFailed extends TransactionEvent {

  String fromAccountId();

  String toAccountId();

  static ImmutableFinishTransactionFailed.Builder builder() {
    return ImmutableFinishTransactionFailed.builder();
  }
}

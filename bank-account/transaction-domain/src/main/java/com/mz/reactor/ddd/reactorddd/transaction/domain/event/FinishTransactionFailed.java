package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import org.immutables.value.Value;

/**
 * this event is fired only when someone is trying to finish transaction which is not in proper status
 */
@Value.Immutable
public interface FinishTransactionFailed extends TransactionEvent {

  String fromAccountId();

  String toAccountId();

  static ImmutableFinishTransactionFailed.Builder builder() {
    return ImmutableFinishTransactionFailed.builder();
  }
}

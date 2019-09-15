package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import org.immutables.value.Value;

@Value.Immutable
public interface FinishTransaction extends TransactionCommand {

  String fromAccountId();

  String toAccountId();

  static ImmutableFinishTransaction.Builder builder() {
    return ImmutableFinishTransaction.builder();
  }
}

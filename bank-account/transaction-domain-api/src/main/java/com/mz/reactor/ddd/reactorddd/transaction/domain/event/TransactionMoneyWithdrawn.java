package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTransactionMoneyWithdrawn.class)
@JsonDeserialize(as = ImmutableTransactionMoneyWithdrawn.class)
public interface TransactionMoneyWithdrawn extends TransactionEvent {

  static ImmutableTransactionMoneyWithdrawn.Builder builder() {
    return ImmutableTransactionMoneyWithdrawn.builder();
  }
}

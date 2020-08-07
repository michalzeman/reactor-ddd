package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTransactionMoneyDeposited.class)
@JsonDeserialize(as = ImmutableTransactionMoneyDeposited.class)
public interface TransactionMoneyDeposited extends TransactionEvent {

  static ImmutableTransactionMoneyDeposited.Builder builder() {
    return ImmutableTransactionMoneyDeposited.builder();
  }
}

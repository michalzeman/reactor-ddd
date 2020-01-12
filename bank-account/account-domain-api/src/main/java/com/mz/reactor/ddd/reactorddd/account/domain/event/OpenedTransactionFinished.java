package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableOpenedTransactionFinished.class)
@JsonDeserialize(as = ImmutableOpenedTransactionFinished.class)
public interface OpenedTransactionFinished extends AccountEvent {

  String transactionId();

  static ImmutableOpenedTransactionFinished.Builder builder() {
    return ImmutableOpenedTransactionFinished.builder();
  }
}

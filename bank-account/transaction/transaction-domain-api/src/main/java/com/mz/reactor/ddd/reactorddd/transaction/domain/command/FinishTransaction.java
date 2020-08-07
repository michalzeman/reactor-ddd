package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableFinishTransaction.class)
@JsonDeserialize(as = ImmutableFinishTransaction.class)
public interface FinishTransaction extends TransactionCommand {

  String fromAccountId();

  String toAccountId();

  static ImmutableFinishTransaction.Builder builder() {
    return ImmutableFinishTransaction.builder();
  }
}

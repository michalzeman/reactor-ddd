package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.concurrent.TransferQueue;

@Value.Immutable
@JsonSerialize(as = ImmutableCancelTransaction.class)
@JsonDeserialize(as = ImmutableCancelTransaction.class)
public interface CancelTransaction extends TransactionCommand {

  String fromAccountId();

  String toAccountId();

  static ImmutableCancelTransaction.Builder builder() {
    return ImmutableCancelTransaction.builder();
  }

}

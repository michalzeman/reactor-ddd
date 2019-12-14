package com.mz.reactor.ddd.reactorddd.transaction.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateTransactionResponse.class)
@JsonDeserialize(as = ImmutableCreateTransactionResponse.class)
public interface CreateTransactionResponse {

  TransactionCreated payload();

  static CreateTransactionResponse from(TransactionCreated transactionCreated) {
    return builder()
        .payload(transactionCreated)
        .build();
  }

  static ImmutableCreateTransactionResponse.Builder builder() {
    return ImmutableCreateTransactionResponse.builder();
  }
}

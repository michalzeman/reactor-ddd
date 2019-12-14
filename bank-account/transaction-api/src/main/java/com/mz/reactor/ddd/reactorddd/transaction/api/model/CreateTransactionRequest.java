package com.mz.reactor.ddd.reactorddd.transaction.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateTransactionRequest.class)
@JsonDeserialize(as = ImmutableCreateTransactionRequest.class)
public interface CreateTransactionRequest {

  CreateTransaction payload();

  static ImmutableCreateTransactionRequest.Builder builder() {
    return ImmutableCreateTransactionRequest.builder();
  }
}

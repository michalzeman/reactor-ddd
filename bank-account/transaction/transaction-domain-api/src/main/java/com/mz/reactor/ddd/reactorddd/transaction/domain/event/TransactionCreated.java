package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableTransactionCreated.class)
@JsonDeserialize(as = ImmutableTransactionCreated.class)
public interface TransactionCreated extends TransactionEvent {
  
  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

  static ImmutableTransactionCreated.Builder builder() {
    return ImmutableTransactionCreated.builder();
  }
}

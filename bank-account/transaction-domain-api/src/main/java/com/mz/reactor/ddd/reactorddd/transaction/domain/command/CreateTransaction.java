package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateTransaction.class)
@JsonDeserialize(as = ImmutableCreateTransaction.class)
public interface CreateTransaction extends TransactionCommand {

  @Value.Default
  default String aggregateId() {
    return UUID.randomUUID().toString();
  }

  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

  static ImmutableCreateTransaction.Builder builder() {
    return ImmutableCreateTransaction.builder();
  }
}

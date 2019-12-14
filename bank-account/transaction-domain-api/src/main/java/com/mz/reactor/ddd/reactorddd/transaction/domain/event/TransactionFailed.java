package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CancelTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableTransactionFailed.class)
@JsonDeserialize(as = ImmutableTransactionFailed.class)
public interface TransactionFailed extends TransactionEvent {

  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

  static ImmutableTransactionFailed.Builder builder() {
    return ImmutableTransactionFailed.builder();
  }

  static TransactionFailed from(CreateTransaction command) {
    return TransactionFailed.builder()
        .toAccountId(command.toAccountId())
        .fromAccountId(command.fromAccountId())
        .aggregateId(command.aggregateId())
        .correlationId(command.correlationId())
        .amount(command.amount())
        .build();
  }
}

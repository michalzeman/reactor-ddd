package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface CreateTransaction extends TransactionCommand {

  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

  static ImmutableCreateTransaction.Builder builder() {
    return ImmutableCreateTransaction.builder();
  }
}

package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTransactionWithdrawRolledBack.class)
@JsonDeserialize(as = ImmutableTransactionWithdrawRolledBack.class)
public interface TransactionWithdrawRolledBack extends TransactionRolledBack {

  static ImmutableTransactionWithdrawRolledBack.Builder builder() {
    return ImmutableTransactionWithdrawRolledBack.builder();
  }
}

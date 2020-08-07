package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTransactionDepositRolledBack.class)
@JsonDeserialize(as = ImmutableTransactionDepositRolledBack.class)
public interface TransactionDepositRolledBack extends TransactionRolledBack {

  static ImmutableTransactionDepositRolledBack.Builder builder() {
    return ImmutableTransactionDepositRolledBack.builder();
  }

}

package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableValidateTransactionMoneyWithdraw.class)
@JsonDeserialize(as = ImmutableValidateTransactionMoneyWithdraw.class)
public interface ValidateTransactionMoneyWithdraw extends ValidateTransactionMoneyState {

  static ImmutableValidateTransactionMoneyWithdraw.Builder builder() {
    return ImmutableValidateTransactionMoneyWithdraw.builder();
  }
}

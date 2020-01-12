package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableValidateTransactionMoneyDeposit.class)
@JsonDeserialize(as = ImmutableValidateTransactionMoneyDeposit.class)
public interface ValidateTransactionMoneyDeposit extends ValidateTransactionMoneyState {

  static ImmutableValidateTransactionMoneyDeposit.Builder builder() {
    return ImmutableValidateTransactionMoneyDeposit.builder();
  }
}

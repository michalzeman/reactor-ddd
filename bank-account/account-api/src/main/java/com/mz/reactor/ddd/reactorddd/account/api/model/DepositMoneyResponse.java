package com.mz.reactor.ddd.reactorddd.account.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDepositMoneyResponse.class)
@JsonDeserialize(as = ImmutableDepositMoneyResponse.class)
public interface DepositMoneyResponse {

  MoneyDeposited payload();

  static ImmutableDepositMoneyResponse.Builder builder() {
    return ImmutableDepositMoneyResponse.builder();
  }

  static DepositMoneyResponse from(MoneyDeposited payload) {
    return builder().payload(payload).build();
  }
}

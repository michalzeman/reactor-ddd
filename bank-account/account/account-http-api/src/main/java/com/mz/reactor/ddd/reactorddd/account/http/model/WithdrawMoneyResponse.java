package com.mz.reactor.ddd.reactorddd.account.http.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyWithdrawn;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableWithdrawMoneyResponse.class)
@JsonDeserialize(as = ImmutableWithdrawMoneyResponse.class)
public interface WithdrawMoneyResponse {

  MoneyWithdrawn payload();

  static ImmutableWithdrawMoneyResponse.Builder builder() {
    return ImmutableWithdrawMoneyResponse.builder();
  }

  static WithdrawMoneyResponse from(MoneyWithdrawn moneyWithdrawn) {
    return builder().payload(moneyWithdrawn).build();
  }
}

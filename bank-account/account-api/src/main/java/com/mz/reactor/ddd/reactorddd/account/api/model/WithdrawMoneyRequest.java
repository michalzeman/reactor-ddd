package com.mz.reactor.ddd.reactorddd.account.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawMoney;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableWithdrawMoneyRequest.class)
@JsonDeserialize(as = ImmutableWithdrawMoneyRequest.class)
public interface WithdrawMoneyRequest {

  WithdrawMoney payload();

  static ImmutableWithdrawMoneyRequest.Builder builder() {
    return ImmutableWithdrawMoneyRequest.builder();
  }
}

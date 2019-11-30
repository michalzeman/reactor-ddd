package com.mz.reactor.ddd.reactorddd.account.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.account.domain.command.DepositMoney;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDepositMoneyRequest.class)
@JsonDeserialize(as = ImmutableDepositMoneyRequest.class)
public interface DepositMoneyRequest {

  DepositMoney payload();

  static ImmutableDepositMoneyRequest.Builder builder() {
    return ImmutableDepositMoneyRequest.builder();
  }
}

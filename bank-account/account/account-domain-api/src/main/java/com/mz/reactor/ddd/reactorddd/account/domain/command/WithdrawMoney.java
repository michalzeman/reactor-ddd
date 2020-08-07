package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableWithdrawMoney.class)
@JsonDeserialize(as = ImmutableWithdrawMoney.class)
public interface WithdrawMoney extends AccountCommand {

  BigDecimal amount();

  static ImmutableWithdrawMoney.Builder builder() {
    return ImmutableWithdrawMoney.builder();
  }
}

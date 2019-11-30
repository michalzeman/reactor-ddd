package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableDepositMoney.class)
@JsonDeserialize(as = ImmutableDepositMoney.class)
public interface DepositMoney extends AccountCommand {
  BigDecimal amount();

  static ImmutableDepositMoney.Builder builder() {
    return ImmutableDepositMoney.builder();
  }
}

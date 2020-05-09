package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableMoneyDeposited.class)
@JsonDeserialize(as = ImmutableMoneyDeposited.class)
public interface MoneyDeposited extends AccountEvent {
  BigDecimal amount();

  static ImmutableMoneyDeposited.Builder builder() {
    return ImmutableMoneyDeposited.builder();
  }
}

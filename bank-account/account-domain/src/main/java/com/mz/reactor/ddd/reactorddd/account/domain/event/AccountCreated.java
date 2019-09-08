package com.mz.reactor.ddd.reactorddd.account.domain.event;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface AccountCreated extends AccountEvent {

  BigDecimal balance();

  static ImmutableAccountCreated.Builder builder() {
    return ImmutableAccountCreated.builder();
  }
}

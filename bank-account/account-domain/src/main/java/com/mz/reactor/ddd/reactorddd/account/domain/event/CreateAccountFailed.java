package com.mz.reactor.ddd.reactorddd.account.domain.event;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface CreateAccountFailed extends AccountEvent {

  BigDecimal balance();

  static ImmutableCreateAccountFailed.Builder builder() {
    return ImmutableCreateAccountFailed.builder();
  }
}

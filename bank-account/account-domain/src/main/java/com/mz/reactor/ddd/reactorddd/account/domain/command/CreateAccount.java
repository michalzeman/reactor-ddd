package com.mz.reactor.ddd.reactorddd.account.domain.command;

import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
public interface CreateAccount extends AccountCommand {

  BigDecimal balance();

  static ImmutableCreateAccount.Builder builder() {
    return ImmutableCreateAccount.builder();
  }
}

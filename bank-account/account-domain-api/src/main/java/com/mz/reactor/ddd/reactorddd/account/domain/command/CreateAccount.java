package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateAccount.class)
@JsonDeserialize(as = ImmutableCreateAccount.class)
public interface CreateAccount extends AccountCommand {

  BigDecimal balance();

  static ImmutableCreateAccount.Builder builder() {
    return ImmutableCreateAccount.builder();
  }
}

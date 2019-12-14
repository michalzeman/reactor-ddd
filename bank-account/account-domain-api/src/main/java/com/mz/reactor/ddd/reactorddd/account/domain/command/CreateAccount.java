package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateAccount.class)
@JsonDeserialize(as = ImmutableCreateAccount.class)
public interface CreateAccount extends AccountCommand {

  @Value.Default
  default String aggregateId() {
    return UUID.randomUUID().toString();
  }

  BigDecimal balance();

  static ImmutableCreateAccount.Builder builder() {
    return ImmutableCreateAccount.builder();
  }
}

package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableAccountCreated.class)
@JsonDeserialize(as = ImmutableAccountCreated.class)
public interface AccountCreated extends AccountEvent {

  BigDecimal balance();

  static ImmutableAccountCreated.Builder builder() {
    return ImmutableAccountCreated.builder();
  }

  static AccountCreated from(CreateAccount createAccount) {
    return builder()
        .aggregateId(createAccount.aggregateId())
        .balance(createAccount.balance())
        .correlationId(createAccount.correlationId())
        .build();
  }
}

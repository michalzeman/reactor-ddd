package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
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

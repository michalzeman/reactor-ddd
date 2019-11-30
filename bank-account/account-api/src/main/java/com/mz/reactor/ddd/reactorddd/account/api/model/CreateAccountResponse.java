package com.mz.reactor.ddd.reactorddd.account.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountCreated;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateAccountResponse.class)
@JsonDeserialize(as = ImmutableCreateAccountResponse.class)
public interface CreateAccountResponse {

  AccountCreated payload();

  static ImmutableCreateAccountResponse.Builder builder() {
    return ImmutableCreateAccountResponse.builder();
  }

  static CreateAccountResponse from(AccountCreated payload) {
    return builder().payload(payload).build();
  }
}

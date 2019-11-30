package com.mz.reactor.ddd.reactorddd.account.api.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateAccountRequest.class)
@JsonDeserialize(as = ImmutableCreateAccountRequest.class)
public interface CreateAccountRequest {

  CreateAccount payload();

  static ImmutableCreateAccountRequest.Builder builder() {
    return ImmutableCreateAccountRequest.builder();
  }
}

package com.mz.reactor.ddd.reactorddd.domain.event;

import org.immutables.value.Value;

@Value.Immutable
public interface AccountCreated extends AccountEvent {


  static ImmutableAccountCreated.Builder builder() {
    return ImmutableAccountCreated.builder();
  }
}

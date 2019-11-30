package com.mz.reactor.ddd.common.api.command;

import org.immutables.value.Value;

@Value.Immutable
public interface CommandResponse<S> {

  CommandResult result();

  S state();

  static ImmutableCommandResponse.Builder builder() {
    return ImmutableCommandResponse.builder();
  }
}

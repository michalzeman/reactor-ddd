package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableAccountNoChanged.class)
@JsonDeserialize(as = ImmutableAccountNoChanged.class)
public interface AccountNoChanged extends AccountEvent {

  static ImmutableAccountNoChanged.Builder builder() {
    return ImmutableAccountNoChanged.builder();
  }
}

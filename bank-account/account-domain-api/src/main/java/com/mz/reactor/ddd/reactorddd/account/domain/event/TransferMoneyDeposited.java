package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableTransferMoneyDeposited.class)
@JsonDeserialize(as = ImmutableTransferMoneyDeposited.class)
public interface TransferMoneyDeposited extends TransferMoneyEvent {

  static ImmutableTransferMoneyDeposited.Builder builder() {
    return ImmutableTransferMoneyDeposited.builder();
  }

}

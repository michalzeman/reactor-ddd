package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDepositTransferMoneyFailed.class)
@JsonDeserialize(as = ImmutableDepositTransferMoneyFailed.class)
public interface DepositTransferMoneyFailed extends TransferMoneyFailed {

  static ImmutableDepositTransferMoneyFailed.Builder builder() {
    return ImmutableDepositTransferMoneyFailed.builder();
  }
}

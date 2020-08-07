package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableWithdrawTransferMoneyFailed.class)
@JsonDeserialize(as = ImmutableWithdrawTransferMoneyFailed.class)
public interface WithdrawTransferMoneyFailed extends TransferMoneyFailed {
  static ImmutableWithdrawTransferMoneyFailed.Builder builder() {
    return ImmutableWithdrawTransferMoneyFailed.builder();
  }
}

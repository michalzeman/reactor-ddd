package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableWithdrawTransferMoney.class)
@JsonDeserialize(as = ImmutableWithdrawTransferMoney.class)
public interface WithdrawTransferMoney extends TransferMoneyCommand {

  static ImmutableWithdrawTransferMoney.Builder builder() {
    return ImmutableWithdrawTransferMoney.builder();
  }

}

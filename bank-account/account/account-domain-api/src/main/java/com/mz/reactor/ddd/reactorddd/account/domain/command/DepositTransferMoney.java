package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDepositTransferMoney.class)
@JsonDeserialize(as = ImmutableDepositTransferMoney.class)
public interface DepositTransferMoney extends TransferMoneyCommand {

  static ImmutableDepositTransferMoney.Builder builder() {
    return ImmutableDepositTransferMoney.builder();
  }

}

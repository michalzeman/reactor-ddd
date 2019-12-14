package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawTransferMoney;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableTransferMoneyWithdrawn.class)
@JsonDeserialize(as = ImmutableTransferMoneyWithdrawn.class)
public interface TransferMoneyWithdrawn extends TransferMoneyEvent {

  static ImmutableTransferMoneyWithdrawn.Builder builder() {
    return ImmutableTransferMoneyWithdrawn.builder();
  }

  static TransferMoneyWithdrawn from(WithdrawTransferMoney cmd) {
    return TransferMoneyWithdrawn.builder()
        .transactionId(cmd.transactionId())
        .aggregateId(cmd.aggregateId())
        .amount(cmd.amount())
        .correlationId(cmd.correlationId())
        .fromAccount(cmd.fromAccount())
        .toAccount(cmd.toAccount())
        .build();
  }

}

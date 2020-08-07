package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableTransferMoneyAccountNotFound.class)
@JsonDeserialize(as = ImmutableTransferMoneyAccountNotFound.class)
public interface TransferMoneyAccountNotFound extends AccountEvent {

  String transactionId();

  static ImmutableTransferMoneyAccountNotFound.Builder builder() {
    return ImmutableTransferMoneyAccountNotFound.builder();
  }
}

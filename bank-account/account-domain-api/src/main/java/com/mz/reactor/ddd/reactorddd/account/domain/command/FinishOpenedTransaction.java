package com.mz.reactor.ddd.reactorddd.account.domain.command;


import org.immutables.value.Value;

@Value.Immutable
public interface FinishOpenedTransaction extends AccountCommand {

  String transactionId();

  static ImmutableFinishOpenedTransaction.Builder builder() {
    return ImmutableFinishOpenedTransaction.builder();
  }

}

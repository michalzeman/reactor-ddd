package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.common.api.view.DomainView;
import org.immutables.value.Value;

import java.math.BigDecimal;

@Value.Immutable
@JsonSerialize(as = ImmutableTransactionState.class)
@JsonDeserialize(as = ImmutableTransactionState.class)
public interface TransactionState extends DomainView {

  default String id() {
    return aggregateId();
  }

  String aggregateId();

  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

  TransactionStatus status();

  static ImmutableTransactionState.Builder builder() {
    return ImmutableTransactionState.builder();
  }

}

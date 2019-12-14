package com.mz.reactor.ddd.reactorddd.account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.reactor.ddd.common.api.view.DomainView;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableAccountState.class)
@JsonDeserialize(as = ImmutableAccountState.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface AccountState extends DomainView {

  default String id() {
    return aggregateId();
  }

  String aggregateId();

  BigDecimal amount();

  @JsonIgnore
  List<String> openedTransactions();

  static ImmutableAccountState.Builder builder() {
    return ImmutableAccountState.builder();
  }
}

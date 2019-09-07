package com.mz.reactor.ddd.reactorddd.domain;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.common.api.valueobject.Money;

public class AccountAggregate {
  private Id aggregateId;

  private Money amount;
}

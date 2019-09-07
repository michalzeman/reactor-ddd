package com.mz.reactor.ddd.common.api.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
  private final BigDecimal amount;

  public Money(BigDecimal amount) {
    this.amount = Objects.requireNonNull(amount, "Money amount can't be null!");
  }

  public BigDecimal getAmount() {
    return amount;
  }
}

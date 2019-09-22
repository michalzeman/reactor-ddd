package com.mz.reactor.ddd.common.api.valueobject;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Money {
  private final BigDecimal amount;

  public Money(BigDecimal amount) {
    this.amount = validateValue.apply(amount);
  }

  public Money depositMoney(BigDecimal amount) {
    return new Money(this.amount.add(amount));
  }

  public Money withdrawMoney(BigDecimal amount) {
    return new Money(this.amount.add(amount.negate()));
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public static final Function<BigDecimal, BigDecimal> validateValue = value ->
      Objects.requireNonNull(value, "Money amount can't be null!");

  public static final Function<BigDecimal, BigDecimal> validateGreaterThenZero = value -> {
    if (BigDecimal.ZERO.compareTo(value) == 0) {
      throw new RuntimeException(String.format("Can't deposit %s money", value));
    }
    return value;
  };

  public static final Function<BigDecimal, BigDecimal> validateDepositMoney = value ->
      validateValue.andThen(validateGreaterThenZero).apply(value);

  public static final BinaryOperator<BigDecimal> validateWithdraw = (currentValue, value) ->
      validateValue
          .andThen(v -> {
            if (currentValue.compareTo(v) < 0) {
              throw new RuntimeException(String.format(
                  "Can not withdraw amount %s, current balance is %s",
                  value,
                  currentValue
              ));
            }
            return v;
          })
          .apply(value);

}

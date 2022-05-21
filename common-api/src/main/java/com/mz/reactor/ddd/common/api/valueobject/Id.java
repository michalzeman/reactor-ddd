package com.mz.reactor.ddd.common.api.valueobject;

import static com.mz.reactor.ddd.common.api.valueobject.StringValues.validateValue;

public record Id(String value)  {
  public Id {
    validateValue.apply(value);
  }

  public static Id of(String id) {
    return new Id(id);
  }

  public static void validate(String fromAccountId, String toAccountId) {
    StringValues.validate(fromAccountId, toAccountId);
  }
}

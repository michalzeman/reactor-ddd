package com.mz.reactor.ddd.common.api.valueobject;

public class Id extends StringValue {
  public Id(String value) {
    super(value);
  }

  public static Id of(String id) {
    return new Id(id);
  }
}

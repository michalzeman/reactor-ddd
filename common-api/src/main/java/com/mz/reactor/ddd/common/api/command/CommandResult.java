package com.mz.reactor.ddd.common.api.command;

import org.immutables.value.Value;

@Value.Immutable
public interface CommandResult {

  enum StatusCode {
    OK,
    BAD_COMMAND,
    NOT_MODIFIED;
  }

}

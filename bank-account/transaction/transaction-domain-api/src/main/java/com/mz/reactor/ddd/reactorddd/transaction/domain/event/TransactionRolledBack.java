package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import java.math.BigDecimal;

public interface TransactionRolledBack extends TransactionEvent {

  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

}

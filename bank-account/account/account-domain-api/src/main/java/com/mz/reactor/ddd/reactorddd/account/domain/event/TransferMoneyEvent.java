package com.mz.reactor.ddd.reactorddd.account.domain.event;

import java.math.BigDecimal;

public interface TransferMoneyEvent extends AccountEvent {

  String transactionId();

  String fromAccountId();

  String toAccountId();

  BigDecimal amount();

}

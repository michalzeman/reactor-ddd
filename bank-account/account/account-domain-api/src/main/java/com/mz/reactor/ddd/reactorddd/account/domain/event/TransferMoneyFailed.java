package com.mz.reactor.ddd.reactorddd.account.domain.event;

import java.math.BigDecimal;

public interface TransferMoneyFailed extends AccountEvent {

  String transactionId();

  String fromAccount();

  String toAccount();

  BigDecimal amount();
}

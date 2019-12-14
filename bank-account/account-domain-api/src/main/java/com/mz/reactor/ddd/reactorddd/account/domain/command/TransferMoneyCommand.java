package com.mz.reactor.ddd.reactorddd.account.domain.command;

import java.math.BigDecimal;

public interface TransferMoneyCommand extends AccountCommand {

  String transactionId();

  String fromAccount();

  String toAccount();

  BigDecimal amount();

}

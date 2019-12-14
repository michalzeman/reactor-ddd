package com.mz.reactor.ddd.reactorddd.account.domain;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.common.api.valueobject.Money;
import com.mz.reactor.ddd.reactorddd.account.domain.command.*;
import com.mz.reactor.ddd.reactorddd.account.domain.event.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountAggregate {
  private Id aggregateId;

  private Money amount;

  private List<Id> openedTransactions = new ArrayList<>();

  public AccountAggregate(String aggregateId) {
    this.aggregateId = new Id(aggregateId);
    this.amount = new Money(BigDecimal.ZERO);
  }

  public MoneyWithdrawn validateWithdrawMoney(WithdrawMoney command) {
    return Money.validateValue
        .andThen(v -> Money.validateWithdraw.apply(this.amount.getAmount(), command.amount()))
        .andThen(v -> MoneyWithdrawn.from(command))
        .apply(command.amount());
  }

  public MoneyDeposited validateDepositMoney(DepositMoney depositMoney) {
    return Money.validateDepositMoney
        .andThen(v ->
            MoneyDeposited.builder()
                .correlationId(depositMoney.correlationId())
                .aggregateId(aggregateId.getValue())
                .amount(v)
                .build())
        .apply(depositMoney.amount());
  }

  public AccountCreated validateCreateAccount(CreateAccount command) {
    return Money.validateValue
        .andThen(v -> AccountCreated.from(command))
        .apply(command.balance());
  }

  public TransferMoneyWithdrawn validateWithdrawTransferMoney(WithdrawTransferMoney command) {
    return Money.validateValue
        .andThen(v -> Money.validateWithdraw.apply(this.amount.getAmount(), command.amount()))
        .andThen(v -> TransferMoneyWithdrawn.from(command))
        .apply(command.amount());
  }

  public TransferMoneyDeposited validateDepositTransferMoney(DepositTransferMoney depositMoney) {
    return Money.validateDepositMoney
        .andThen(v ->
            TransferMoneyDeposited.builder()
                .correlationId(depositMoney.correlationId())
                .aggregateId(aggregateId.getValue())
                .amount(v)
                .transactionId(depositMoney.transactionId())
                .fromAccount(depositMoney.fromAccount())
                .toAccount(depositMoney.toAccount())
                .build())
        .apply(depositMoney.amount());
  }

  public AccountAggregate applyMoneyDeposited(MoneyDeposited moneyDeposited) {
    this.amount = this.amount.depositMoney(moneyDeposited.amount());
    return this;
  }

  public AccountAggregate applyAccountCreated(AccountCreated event) {
    this.amount = this.amount.depositMoney(event.balance());
    return this;
  }

  public AccountAggregate applyMoneyWithdrawn(MoneyWithdrawn event) {
    this.amount = this.amount.withdrawMoney(event.amount());
    return this;
  }

  public AccountAggregate applyTransferMoneyWithdrawn(TransferMoneyWithdrawn event) {
    this.amount = this.amount.withdrawMoney(event.amount());
    return this;
  }

  public AccountAggregate applyTransferMoneyDeposited(TransferMoneyDeposited event) {
    this.amount = this.amount.depositMoney(event.amount());
    return this;
  }

  public AccountState getState() {
    return AccountState.builder()
        .aggregateId(this.aggregateId.getValue())
        .addAllOpenedTransactions(this.openedTransactions.stream().map(Id::getValue).collect(Collectors.toList()))
        .amount(this.amount.getAmount())
        .build();
  }
}

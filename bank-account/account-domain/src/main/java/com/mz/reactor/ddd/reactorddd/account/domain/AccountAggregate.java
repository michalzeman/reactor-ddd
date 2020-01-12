package com.mz.reactor.ddd.reactorddd.account.domain;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.common.api.valueobject.Money;
import com.mz.reactor.ddd.reactorddd.account.domain.command.*;
import com.mz.reactor.ddd.reactorddd.account.domain.event.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountAggregate {
  private Id aggregateId;

  private Money amount;

  private Set<Id> openedTransactions = new HashSet<>();

  private Set<Id> finishedTransactions = new HashSet<>();

  public AccountAggregate(String aggregateId) {
    this.aggregateId = new Id(aggregateId);
    this.amount = new Money(BigDecimal.ZERO);
  }

  public AccountEvent validateFinishOpenedTransaction(FinishOpenedTransaction cmd) {
    if (this.openedTransactions.stream().anyMatch(id -> cmd.transactionId().equals(id.getValue()))) {
      return OpenedTransactionFinished.builder()
          .aggregateId(cmd.aggregateId())
          .correlationId(cmd.correlationId())
          .transactionId(cmd.transactionId())
          .build();
    } else {
      return AccountNoChanged.builder()
          .aggregateId(cmd.aggregateId())
          .correlationId(cmd.correlationId())
          .build();
    }
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

  public AccountEvent validateWithdrawTransferMoney(WithdrawTransferMoney command) {
    if (this.openedTransactions.stream().anyMatch(id -> command.transactionId().equals(id.getValue()))
        || this.finishedTransactions.stream().anyMatch(id -> command.transactionId().equals(id.getValue()))) {
      return AccountNoChanged.builder()
          .aggregateId(command.aggregateId())
          .correlationId(command.correlationId())
          .build();
    }
    return Money.validateValue
        .andThen(v -> Money.validateWithdraw.apply(this.amount.getAmount(), command.amount()))
        .andThen(v -> TransferMoneyWithdrawn.from(command))
        .apply(command.amount());
  }

  public AccountEvent validateDepositTransferMoney(DepositTransferMoney command) {
    if (this.openedTransactions.stream().anyMatch(id -> command.transactionId().equals(id.getValue()))
        || this.finishedTransactions.stream().anyMatch(id -> command.transactionId().equals(id.getValue()))) {
      return AccountNoChanged.builder()
          .aggregateId(command.aggregateId())
          .correlationId(command.correlationId())
          .build();
    }
    return Money.validateDepositMoney
        .andThen(v ->
            TransferMoneyDeposited.builder()
                .correlationId(command.correlationId())
                .aggregateId(aggregateId.getValue())
                .amount(v)
                .transactionId(command.transactionId())
                .fromAccountId(command.fromAccount())
                .toAccountId(command.toAccount())
                .build())
        .apply(command.amount());
  }

  public AccountAggregate applyOpenedTransactionFinished(OpenedTransactionFinished event) {
    this.openedTransactions = this.openedTransactions.stream()
        .filter(id -> !event.transactionId().equals(id.getValue()))
        .collect(Collectors.toSet());
    this.finishedTransactions.add(Id.of(event.transactionId()));
    return this;
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
    this.openedTransactions.add(Id.of(event.transactionId()));
    return this;
  }

  public AccountAggregate applyTransferMoneyDeposited(TransferMoneyDeposited event) {
    this.amount = this.amount.depositMoney(event.amount());
    this.openedTransactions.add(Id.of(event.transactionId()));
    return this;
  }

  public AccountState getState() {
    return AccountState.builder()
        .aggregateId(this.aggregateId.getValue())
        .addAllOpenedTransactions(this.openedTransactions.stream().map(Id::getValue).collect(Collectors.toSet()))
        .addAllFinishedTransactions(this.finishedTransactions.stream().map(Id::getValue).collect(Collectors.toSet()))
        .amount(this.amount.getAmount())
        .build();
  }
}

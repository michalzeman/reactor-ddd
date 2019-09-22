package com.mz.reactor.ddd.reactorddd.account.domain;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.common.api.valueobject.Money;
import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import com.mz.reactor.ddd.reactorddd.account.domain.command.DepositMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountCreated;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyWithdrawn;
import org.apache.commons.lang3.text.translate.AggregateTranslator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

  public AccountState getState() {
    return AccountState.builder()
        .aggregateId(this.aggregateId.getValue())
        .addAllOpenedTransactions(this.openedTransactions.stream().map(Id::getValue).collect(Collectors.toList()))
        .amount(this.amount.getAmount())
        .build();
  }
}

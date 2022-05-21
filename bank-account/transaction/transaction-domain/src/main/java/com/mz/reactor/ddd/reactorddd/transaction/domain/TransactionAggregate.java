package com.mz.reactor.ddd.reactorddd.transaction.domain;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.*;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionAggregate {

  private final Id aggregateId;

  private Id fromAccount;

  private Id toAccount;

  private BigDecimal amount;

  private TransactionStatus status;

  private boolean moneyWithdrawn;

  private boolean moneyDeposited;

  public TransactionAggregate(String aggregateId) {
    this.aggregateId = new Id(aggregateId);
    status = TransactionStatus.INITIALIZED;
  }

  public List<TransactionEvent> validateTransactionMoneyDeposit(ValidateTransactionMoneyDeposit cmd) {
    if (this.status == TransactionStatus.FAILED) {
      return List.of(TransactionDepositRolledBack.builder()
          .aggregateId(this.aggregateId.value())
          .amount(this.amount)
          .correlationId(cmd.correlationId())
          .toAccountId(this.toAccount.value())
          .fromAccountId(this.fromAccount.value())
          .build());
    } else {
      return handleFinishWhenMoneyDeposited(cmd.correlationId().orElseGet(() -> UUID.randomUUID().toString()))
          .apply(List.of(TransactionMoneyDeposited.builder()
              .aggregateId(this.aggregateId.value())
              .correlationId(cmd.correlationId())
              .build()));
    }
  }

  public List<TransactionEvent> validateTransactionMoneyWithdraw(ValidateTransactionMoneyWithdraw cmd) {
    if (this.status == TransactionStatus.FAILED) {
      return List.of(TransactionWithdrawRolledBack.builder()
          .aggregateId(this.aggregateId.value())
          .amount(this.amount)
          .correlationId(cmd.correlationId())
          .toAccountId(this.toAccount.value())
          .fromAccountId(this.fromAccount.value())
          .build());
    } else {
      return handleFinishWhenMoneyWithdrawn(cmd.correlationId().orElseGet(() -> UUID.randomUUID().toString()))
          .apply(List.of(TransactionMoneyWithdrawn.builder()
              .aggregateId(this.aggregateId.value())
              .correlationId(cmd.correlationId())
              .build()));
    }
  }

  public TransactionCreated validateCreateTransaction(CreateTransaction command) {
    if (status == TransactionStatus.INITIALIZED) {
      Id.validate(command.fromAccountId(), command.toAccountId());
      if (command.amount().compareTo(BigDecimal.ZERO) <= 0) {
        throw new RuntimeException(String.format("Amount can't be %s", command.amount()));
      }

      return TransactionCreated.builder()
          .aggregateId(this.aggregateId.value())
          .correlationId(command.correlationId())
          .amount(command.amount())
          .fromAccountId(command.fromAccountId())
          .toAccountId(command.toAccountId())
          .build();
    } else {
      throw new RuntimeException(String.format("Can't be applied command %s, aggregate is in TransactionStatus %s",
          command, this.status));
    }
  }

  public TransactionFinished validateFinishTransaction(FinishTransaction command) {
    if (status == TransactionStatus.CREATED) {
      return TransactionFinished.builder()
          .aggregateId(aggregateId.value())
          .correlationId(command.correlationId())
          .fromAccountId(fromAccount.value())
          .toAccountId(toAccount.value())
          .build();
    } else {
      throw new RuntimeException(String.format("Transaction in the state: %s can't be finished!", status));
    }
  }

  public List<TransactionEvent> validateCancelTransaction(CancelTransaction command) {
    if (status == TransactionStatus.CREATED) {
      var correlationId = command.correlationId().orElseGet(() -> UUID.randomUUID().toString());
      return handleMoneyDepositedDuringCancel(correlationId)
          .andThen(handleMoneyWithdrawnDuringCancel(correlationId))
          .apply(List.of(TransactionFailed.builder()
              .aggregateId(aggregateId.value())
              .correlationId(command.correlationId())
              .fromAccountId(fromAccount.value())
              .toAccountId(toAccount.value())
              .amount(this.amount)
              .build())
          );
    } else {
      throw new RuntimeException(String.format("Transaction in the state: %s can't be canceled!", status));
    }
  }

  public TransactionAggregate applyTransactionCreated(TransactionCreated created) {
    this.fromAccount = new Id(created.fromAccountId());
    this.toAccount = new Id(created.toAccountId());
    this.amount = created.amount();
    this.status = TransactionStatus.CREATED;
    return this;
  }

  public TransactionAggregate applyTransactionFinished() {
    this.status = TransactionStatus.FINISHED;
    return this;
  }

  public TransactionAggregate applyTransactionFailed() {
    this.status = TransactionStatus.FAILED;
    return this;
  }

  public TransactionAggregate applyTransactionMoneyDeposited() {
    this.moneyDeposited = true;
    return this;
  }

  public TransactionAggregate applyTransactionMoneyWithdrawn() {
    this.moneyWithdrawn = true;
    return this;
  }

  public TransactionState getState() {
    return TransactionState.builder()
        .amount(amount)
        .fromAccountId(fromAccount.value())
        .toAccountId(toAccount.value())
        .aggregateId(aggregateId.value())
        .moneyDeposited(moneyDeposited)
        .moneyWithdrawn(moneyWithdrawn)
        .status(this.status)
        .build();
  }

  private Function<List<TransactionEvent>, List<TransactionEvent>> handleMoneyDepositedDuringCancel(String correlationId) {
    return events -> {
      if (moneyDeposited) {
        return Stream.concat(events.stream(), Stream.of(TransactionDepositRolledBack.builder()
            .aggregateId(this.aggregateId.value())
            .amount(this.amount)
            .correlationId(correlationId)
            .toAccountId(this.toAccount.value())
            .fromAccountId(this.fromAccount.value())
            .build())).collect(Collectors.toList());
      } else {
        return events;
      }
    };
  }

  private Function<List<TransactionEvent>, List<TransactionEvent>> handleMoneyWithdrawnDuringCancel(String correlationId) {
    return events -> {
      if (moneyWithdrawn) {
        return Stream.concat(events.stream(), Stream.of(TransactionWithdrawRolledBack.builder()
            .aggregateId(this.aggregateId.value())
            .amount(this.amount)
            .correlationId(correlationId)
            .toAccountId(this.toAccount.value())
            .fromAccountId(this.fromAccount.value())
            .build())).collect(Collectors.toList());
      } else {
        return events;
      }
    };
  }

  private Function<List<TransactionEvent>, List<TransactionEvent>> handleFinishWhenMoneyWithdrawn(String correlationId) {
    return events -> {
      if (moneyDeposited) {
        return Stream.concat(
            events.stream(),
            Stream.of(TransactionFinished.builder()
                .aggregateId(aggregateId.value())
                .correlationId(correlationId)
                .fromAccountId(fromAccount.value())
                .toAccountId(toAccount.value())
                .build()))
            .collect(Collectors.toList());
      } else {
        return events;
      }
    };
  }

  private Function<List<TransactionEvent>, List<TransactionEvent>> handleFinishWhenMoneyDeposited(String correlationId) {
    return events -> {
      if (moneyWithdrawn) {
        return Stream.concat(
            events.stream(),
            Stream.of(TransactionFinished.builder()
                .aggregateId(aggregateId.value())
                .correlationId(correlationId)
                .fromAccountId(fromAccount.value())
                .toAccountId(toAccount.value())
                .build()))
            .collect(Collectors.toList());
      } else {
        return events;
      }
    };
  }
}

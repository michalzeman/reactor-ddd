package com.mz.reactor.ddd.reactorddd.transaction.adapters.transaction;

import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.account.api.AccountApplicationService;
import com.mz.reactor.ddd.reactorddd.account.api.AccountQuery;
import com.mz.reactor.ddd.reactorddd.account.domain.command.DepositMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.DepositTransferMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawTransferMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyAccountNotFound;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyWithdrawn;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionDepositRolledBack;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionWithdrawRolledBack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class TransactionChangeStreamAdapter {

  private static final Log log = LogFactory.getLog(TransactionChangeStreamAdapter.class);

  private final ApplicationMessageBus messageBus;

  private final AccountApplicationService accountService;

  private final AccountQuery accountQuery;

  public TransactionChangeStreamAdapter(ApplicationMessageBus messageBus, AccountApplicationService accountService, AccountQuery accountQuery) {
    this.messageBus = Objects.requireNonNull(messageBus);
    this.accountService = Objects.requireNonNull(accountService);
    this.accountQuery = Objects.requireNonNull(accountQuery);
    handleTransactionCreated(messageBus);
    handleTransactionDepositRolledBack(messageBus);
    handleTransactionWithdrawRolledBack(messageBus);
  }

  private void handleTransactionCreated(ApplicationMessageBus bus) {
    bus.messagesStream()
        .filter(m -> m instanceof TransactionCreated)
        .cast(TransactionCreated.class)
        .filterWhen(e -> accountQuery.findById(e.fromAccountId())
            .hasElement()
            .doOnSuccess(i -> ifAccountNotExistPublishEvent(i, e.fromAccountId(), e.aggregateId(), e.correlationId()
                .orElseGet(() -> UUID.randomUUID().toString())))
        )
        .flatMap(e -> accountService.execute(WithdrawTransferMoney.builder()
            .aggregateId(e.fromAccountId())
            .transactionId(e.aggregateId())
            .correlationId(e.correlationId())
            .fromAccount(e.fromAccountId())
            .toAccount(e.toAccountId())
            .amount(e.amount())
            .build(), TransferMoneyWithdrawn.class))
        .filterWhen(e -> accountQuery.findById(e.toAccountId())
            .hasElement()
            .doOnSuccess(i -> ifAccountNotExistPublishEvent(i, e.toAccountId(), e.transactionId(), e.correlationId()
                .orElseGet(() -> UUID.randomUUID().toString())))
        )
        .flatMap(e -> accountService.execute(DepositTransferMoney.builder()
            .aggregateId(e.toAccountId())
            .transactionId(e.transactionId())
            .correlationId(e.correlationId())
            .fromAccount(e.fromAccountId())
            .toAccount(e.toAccountId())
            .amount(e.amount())
            .build(), TransferMoneyDeposited.class))
        .doOnError(e -> log.error("handleTransactionCreated -> ", e))
        .retry()
        .subscribe();
  }

  private void handleTransactionWithdrawRolledBack(ApplicationMessageBus bus) {
    bus.messagesStream()
        .filter(m -> m instanceof TransactionWithdrawRolledBack)
        .cast(TransactionWithdrawRolledBack.class)
        .filterWhen(e -> accountQuery.findById(e.fromAccountId()).hasElement())
        .flatMap(e -> accountService.execute(DepositMoney.builder()
            .amount(e.amount())
            .aggregateId(e.fromAccountId())
            .correlationId(e.correlationId())
            .build(), MoneyDeposited.class))
        .log()
        .retry()
        .subscribe();
  }

  private void handleTransactionDepositRolledBack(ApplicationMessageBus bus) {
    bus.messagesStream()
        .filter(m -> m instanceof TransactionDepositRolledBack)
        .cast(TransactionDepositRolledBack.class)
        .filterWhen(e -> accountQuery.findById(e.toAccountId()).hasElement())
        .flatMap(e -> accountService.execute(WithdrawMoney.builder()
            .amount(e.amount())
            .aggregateId(e.toAccountId())
            .correlationId(e.correlationId())
            .build(), MoneyDeposited.class))
        .log()
        .retry()
        .subscribe();
  }

  private void ifAccountNotExistPublishEvent(
      Boolean isAccount,
      String accountId,
      String transactionId,
      String correlationId
  ) {
    if (!isAccount) {
      messageBus.publishMessage(TransferMoneyAccountNotFound.builder()
          .aggregateId(accountId)
          .correlationId(correlationId)
          .transactionId(transactionId)
          .build());
    }
  }
}

package com.mz.reactor.ddd.reactorddd.transaction.adapters.account;

import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyAccountNotFound;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyFailed;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyWithdrawn;
import com.mz.reactor.ddd.reactorddd.transaction.api.TransactionApplicationService;
import com.mz.reactor.ddd.reactorddd.transaction.api.TransactionQuery;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CancelTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.ValidateTransactionMoneyDeposit;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.ValidateTransactionMoneyWithdraw;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionEvent;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AccountChangeStreamAdapter {

  private static final Log log = LogFactory.getLog(AccountChangeStreamAdapter.class);

  private final TransactionApplicationService transactionApplicationService;

  private final TransactionQuery query;

  public AccountChangeStreamAdapter(
      ApplicationMessageBus messageBus,
      TransactionApplicationService transactionApplicationService,
      TransactionQuery query
  ) {
    Objects.requireNonNull(messageBus);
    this.transactionApplicationService = Objects.requireNonNull(transactionApplicationService);
    this.query = Objects.requireNonNull(query);
    handleTransferMoneyDeposited(messageBus);
    handleTransferMoneyWithdrawn(messageBus);
    handleTransferMoneyFailed(messageBus);
    handleTransferMoneyAccountNotFound(messageBus);
  }

  private void handleTransferMoneyDeposited(ApplicationMessageBus bus) {
    log.debug("handleTransferMoneyDeposited ->");
    bus.messagesStream()
        .filter(m -> m instanceof TransferMoneyDeposited)
        .cast(TransferMoneyDeposited.class)
        .filterWhen(e -> query.findById(e.transactionId()).hasElement())
        .flatMap(e -> transactionApplicationService.execute(ValidateTransactionMoneyDeposit.builder()
            .aggregateId(e.transactionId())
            .accountId(e.aggregateId())
            .correlationId(e.correlationId())
            .build(), TransactionEvent.class))
        .doOnError(error -> log.error("handleTransferMoneyDeposited -> ", error))
        .log()
        .retry()
        .subscribe();
  }

  private void handleTransferMoneyWithdrawn(ApplicationMessageBus bus) {
    log.debug("handleTransferMoneyDeposited ->");
    bus.messagesStream()
        .filter(m -> m instanceof TransferMoneyWithdrawn)
        .cast(TransferMoneyWithdrawn.class)
        .filterWhen(e -> query.findById(e.transactionId()).hasElement())
        .flatMap(e -> transactionApplicationService.execute(ValidateTransactionMoneyWithdraw.builder()
            .aggregateId(e.transactionId())
            .accountId(e.aggregateId())
            .correlationId(e.correlationId())
            .build(), TransactionEvent.class))
        .doOnError(error -> log.error("handleTransferMoneyWithdrawn -> ", error))
        .log()
        .retry()
        .subscribe();
  }

  private void handleTransferMoneyFailed(ApplicationMessageBus bus) {
    log.debug("handleTransferMoneyFailed ->");
    bus.messagesStream()
        .filter(m -> m instanceof TransferMoneyFailed)
        .cast(TransferMoneyFailed.class)
        .flatMap(e -> query.findById(e.transactionId()))
        .flatMap(s -> transactionApplicationService.execute(CancelTransaction.builder()
            .aggregateId(s.aggregateId())
            .fromAccountId(s.fromAccountId())
            .toAccountId(s.toAccountId())
            .build(), TransactionFailed.class))
        .doOnError(error -> log.error("handleTransferMoneyFailed -> ", error))
        .log()
        .retry()
        .subscribe();
  }

  private void handleTransferMoneyAccountNotFound(ApplicationMessageBus bus) {
    bus.messagesStream()
        .filter(m -> m instanceof TransferMoneyAccountNotFound)
        .cast(TransferMoneyAccountNotFound.class)
        .flatMap(e -> query.findById(e.transactionId()))
        .flatMap(s -> transactionApplicationService.execute(CancelTransaction.builder()
            .aggregateId(s.aggregateId())
            .fromAccountId(s.fromAccountId())
            .toAccountId(s.toAccountId())
            .build(), TransactionFailed.class))
        .doOnError(error -> log.error("handleTransferMoneyFailed -> ", error))
        .log()
        .retry()
        .subscribe();
  }
}

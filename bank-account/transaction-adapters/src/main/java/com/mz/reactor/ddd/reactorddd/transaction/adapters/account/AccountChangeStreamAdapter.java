package com.mz.reactor.ddd.reactorddd.transaction.adapters.account;

import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.TransferMoneyFailed;
import com.mz.reactor.ddd.reactorddd.transaction.api.TransactionApplicationService;
import com.mz.reactor.ddd.reactorddd.transaction.api.TransactionQuery;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CancelTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.FinishTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AccountChangeStreamAdapter {

  private static final Log log = LogFactory.getLog(AccountChangeStreamAdapter.class);

  private final ApplicationMessageBus messageBus;

  private final TransactionApplicationService transactionApplicationService;

  private final TransactionQuery query;

  public AccountChangeStreamAdapter(
      ApplicationMessageBus messageBus,
      TransactionApplicationService transactionApplicationService,
      TransactionQuery query
  ) {
    this.messageBus = Objects.requireNonNull(messageBus);
    this.transactionApplicationService = Objects.requireNonNull(transactionApplicationService);
    this.query = Objects.requireNonNull(query);
    handleTransferMoneyDeposited(messageBus);
    handleTransferMoneyFailed(messageBus);
  }

  private void handleTransferMoneyDeposited(ApplicationMessageBus bus) {
    log.debug("handleTransferMoneyDeposited ->");
    bus.messagesStream()
        .filter(m -> m instanceof TransferMoneyDeposited)
        .cast(TransferMoneyDeposited.class)
        .flatMap(e -> query.findById(e.transactionId()))
        .flatMap(s -> transactionApplicationService.execute(FinishTransaction.builder()
            .aggregateId(s.aggregateId())
            .fromAccountId(s.fromAccountId())
            .toAccountId(s.toAccountId())
            .build()))
        .doOnError(error -> log.error("handleMoneyDeposited -> ", error))
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
}

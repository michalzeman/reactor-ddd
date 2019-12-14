package com.mz.reactor.ddd.reactorddd.account.impl;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.account.api.AccountApplicationService;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import com.mz.reactor.ddd.reactorddd.account.domain.command.*;
import com.mz.reactor.ddd.reactorddd.account.domain.event.*;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class AccountApplicationServiceImpl implements AccountApplicationService {

  private static final Log log = LogFactory.getLog(AccountApplicationServiceImpl.class);

  private final AggregateFacade<AccountAggregate, AccountCommand, AccountState> aggregateFacade;

  public AccountApplicationServiceImpl(
      ApplicationMessageBus bus,
      @Qualifier("accountAggregateFacade") AggregateFacade<AccountAggregate, AccountCommand, AccountState> aggregateFacade
  ) {
    this.aggregateFacade = aggregateFacade;
    handleTransactionCreated(bus);
    handleTransferMoneyWithdrawn(bus);
  }

  public <R extends DomainEvent> Mono<R> execute(AccountCommand cmd, Class<R> eventType) {
    return this.aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId(), eventType)
        .cast(eventType);
  }

  @Override
  public Mono<AccountCreated> execute(CreateAccount cmd) {
    return this.aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId(), AccountCreated.class)
        .cast(AccountCreated.class);
  }

  @Override
  public Mono<MoneyDeposited> execute(DepositMoney cmd) {
    return this.aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId(), MoneyDeposited.class)
        .cast(MoneyDeposited.class);
  }

  @Override
  public Mono<MoneyWithdrawn> execute(WithdrawMoney cmd) {
    return this.aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId(), MoneyWithdrawn.class)
        .cast(MoneyWithdrawn.class);
  }

//  private void handleTransactionFailed(ApplicationMessageBus bus) {
//    bus.messagesStream()
//        .filter(m -> m instanceof TransactionFailed)
//        .cast(TransactionFailed.class)
//        .flatMap(e -> execute(DepositMoney.builder()
//            .correlationId(e.correlationId())
//            .aggregateId(e.fromAccountId())
//            .amount(e.amount())
//            .build()))
//        .map(Optional::of)
//        .doOnError(e -> log.error("handleTransactionFailed -> ", e))
//        .retry()
//        .subscribe();
//  }

  private void handleTransactionCreated(ApplicationMessageBus bus) {
    bus.messagesStream()
        .filter(m -> m instanceof TransactionCreated)
        .cast(TransactionCreated.class)
        .flatMap(e -> execute(WithdrawTransferMoney.builder()
            .aggregateId(e.fromAccountId())
            .transactionId(e.aggregateId())
            .correlationId(e.correlationId())
            .fromAccount(e.fromAccountId())
            .toAccount(e.toAccountId())
            .amount(e.amount())
            .build(), TransferMoneyWithdrawn.class))
        .doOnError(e -> log.error("handleTransactionCreated -> ", e))
        .retry()
        .subscribe();
  }

  private void handleTransferMoneyWithdrawn(ApplicationMessageBus bus) {
    bus.messagesStream()
        .filter(m -> m instanceof TransferMoneyWithdrawn)
        .cast(TransferMoneyWithdrawn.class)
        .flatMap(e -> execute(DepositTransferMoney.builder()
            .aggregateId(e.toAccount())
            .transactionId(e.transactionId())
            .correlationId(e.correlationId())
            .fromAccount(e.fromAccount())
            .toAccount(e.toAccount())
            .amount(e.amount())
            .build(), TransferMoneyDeposited.class))
        .doOnError(e -> log.error("handleTransferMoneyWithdrawn -> ", e))
        .retry()
        .subscribe();
  }
}

package com.mz.reactor.ddd.reactorddd.transaction.impl;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.account.domain.command.AccountCommand;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import com.mz.reactor.ddd.reactorddd.transaction.api.TransactionApplicationService;
import com.mz.reactor.ddd.reactorddd.transaction.api.TransactionQuery;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionState;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.FinishTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.TransactionCommand;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFinished;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

import static com.mz.reactor.ddd.reactorddd.transaction.wiring.TransactionConfiguration.TRANSACTION_AGGREGATE_FACADE;

@Service
public class TransactionApplicationServiceImpl implements TransactionApplicationService {

  private static final Log log = LogFactory.getLog(TransactionApplicationServiceImpl.class);

  private final AggregateFacade<TransactionAggregate, TransactionCommand, TransactionState> aggregateFacade;

  public TransactionApplicationServiceImpl(
      @Qualifier(TRANSACTION_AGGREGATE_FACADE) AggregateFacade<TransactionAggregate, TransactionCommand, TransactionState> aggregateFacade,
      ApplicationMessageBus bus,
      TransactionQuery query
  ) {
    this.aggregateFacade = Objects.requireNonNull(aggregateFacade);
  }

  @Override
  public Mono<TransactionCreated> execute(CreateTransaction createTransaction) {
    return aggregateFacade.executeReturnEvent(createTransaction, UUID.randomUUID().toString(), TransactionCreated.class)
        .cast(TransactionCreated.class);
  }

  @Override
  public Mono<TransactionFinished> execute(FinishTransaction cmd) {
    return aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId(), TransactionFinished.class)
        .cast(TransactionFinished.class);
  }

  @Override
  public <R extends DomainEvent> Mono<R> execute(TransactionCommand cmd, Class<R> eventType) {
    return this.aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId(), eventType)
        .cast(eventType);
  }
}

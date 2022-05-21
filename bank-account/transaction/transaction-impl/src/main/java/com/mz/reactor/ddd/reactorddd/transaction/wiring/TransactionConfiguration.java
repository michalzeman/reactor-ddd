package com.mz.reactor.ddd.reactorddd.transaction.wiring;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateRepository;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl.AggregateFacadeImpl;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl.AggregateRepositoryImpl;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.ViewRepository;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.impl.ViewRepositoryImpl;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionCommandHandler;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionEventHandler;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionState;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.TransactionCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class TransactionConfiguration {

  public static final String TRANSACTION_AGGREGATE_REPOSITORY = "transactionAggregateRepository";
  public static final String TRANSACTION_AGGREGATE_FACADE = "transactionAggregateFacade";
  public static final String TRANSACTION_VIEW_REPOSITORY = "transactionViewRepository";

  private final TransactionEventHandler transactionEventHandler = new TransactionEventHandler();
  private final TransactionCommandHandler transactionCommandHandler = new TransactionCommandHandler();
  private final Function<Id, TransactionAggregate> aggregateFactory = id -> new TransactionAggregate(id.value());
  private final Function<TransactionAggregate, TransactionState> stateFactory = TransactionAggregate::getState;

  @Bean(TRANSACTION_AGGREGATE_REPOSITORY)
  public AggregateRepository<TransactionAggregate, TransactionCommand, TransactionState> getAggregateRepository() {
    return new AggregateRepositoryImpl<>(transactionCommandHandler, transactionEventHandler, aggregateFactory, stateFactory);
  }

  @Bean(TRANSACTION_AGGREGATE_FACADE)
  public AggregateFacade<TransactionAggregate, TransactionCommand, TransactionState> getAggregateFacade(
      @Qualifier(TRANSACTION_AGGREGATE_REPOSITORY) AggregateRepository<TransactionAggregate, TransactionCommand, TransactionState> aggregateRepository,
      @Qualifier(TRANSACTION_VIEW_REPOSITORY) ViewRepository<TransactionState> viewRepository,
      ApplicationMessageBus bus
  ) {
    return new AggregateFacadeImpl<>(aggregateRepository, bus::publishMessage, bus::publishMessage);
  }

  @Bean(TRANSACTION_VIEW_REPOSITORY)
  public ViewRepository<TransactionState> getTransactionViewRepository() {
    return new ViewRepositoryImpl<>();
  }
}

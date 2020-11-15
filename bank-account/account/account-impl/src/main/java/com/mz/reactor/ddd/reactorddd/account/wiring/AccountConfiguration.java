package com.mz.reactor.ddd.reactorddd.account.wiring;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountCommandHandler;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountEventHandler;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import com.mz.reactor.ddd.reactorddd.account.domain.command.AccountCommand;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateRepository;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl.AggregateFacadeImpl;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl.AggregateRepositoryImpl;
import com.mz.reactor.ddd.reactorddd.persistance.query.Query;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.ViewRepository;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.impl.ViewRepositoryImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class AccountConfiguration {

  public static final String ACCOUNT_AGGREGATE_REPOSITORY = "accountAggregateRepository";
  public static final String ACCOUNT_AGGREGATE_FACADE = "accountAggregateFacade";
  public static final String ACCOUNT_VIEW_REPOSITORY = "accountViewRepository";
  public static final String ACCOUNT_QUERY_SERVICE = "accountQueryService";

  private final AccountEventHandler accountEventApplier = new AccountEventHandler();
  private final AccountCommandHandler accountCommandHandler = new AccountCommandHandler();
  private final Function<Id, AccountAggregate> aggregateFactory = id -> new AccountAggregate(id.getValue());
  private final Function<AccountAggregate, AccountState> stateFactory = AccountAggregate::getState;

  @Bean(ACCOUNT_QUERY_SERVICE)
  public Query<AccountState> accountQueryService(
      @Qualifier(ACCOUNT_VIEW_REPOSITORY) ViewRepository<AccountState> viewRepository,
      ApplicationMessageBus bus
  ) {
    return Query.of(
        viewRepository,
        () -> bus.messagesStream()
            .filter(m -> m instanceof AccountState)
            .cast(AccountState.class)
    );
  }

  @Bean(ACCOUNT_AGGREGATE_REPOSITORY)
  public AggregateRepository<AccountAggregate, AccountCommand, AccountState> getAggregateRepository() {
    return new AggregateRepositoryImpl<>(accountCommandHandler, accountEventApplier, aggregateFactory, stateFactory);
  }

  @Bean(ACCOUNT_AGGREGATE_FACADE)
  public AggregateFacade<AccountAggregate, AccountCommand, AccountState> getAggregateFacade(
      @Qualifier(ACCOUNT_AGGREGATE_REPOSITORY) AggregateRepository<AccountAggregate, AccountCommand, AccountState> aggregateRepository,
      @Qualifier(ACCOUNT_VIEW_REPOSITORY) ViewRepository<AccountState> viewRepository,
      ApplicationMessageBus bus
  ) {
    return new AggregateFacadeImpl<>(aggregateRepository, bus::publishMessage, bus::publishMessage);
  }

  @Bean(ACCOUNT_VIEW_REPOSITORY)
  public ViewRepository<AccountState> getViewRepository() {
    return new ViewRepositoryImpl<AccountState>();
  }

}

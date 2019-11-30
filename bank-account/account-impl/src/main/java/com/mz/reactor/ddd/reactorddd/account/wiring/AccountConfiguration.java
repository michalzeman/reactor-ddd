package com.mz.reactor.ddd.reactorddd.account.wiring;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.common.components.bus.impl.ApplicationMessageBusImpl;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountCommandHandler;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountEventApplier;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import com.mz.reactor.ddd.reactorddd.account.domain.command.AccountCommand;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountEvent;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateRepository;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl.AggregateFacadeImpl;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl.AggregateRepositoryImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class AccountConfiguration {

  public static final String ACCOUNT_AGGREGATE_REPOSITORY = "accountAggregateRepository";

  private final AccountEventApplier accountEventApplier = new AccountEventApplier();
  private final AccountCommandHandler accountCommandHandler = new AccountCommandHandler();
  private final Function<Id, AccountAggregate> aggregateFactory = id -> new AccountAggregate(id.toString());
  private final Function<AccountAggregate, AccountState> stateFactory = AccountAggregate::getState;

  @Bean(ACCOUNT_AGGREGATE_REPOSITORY)
  public AggregateRepository<AccountAggregate, AccountCommand, AccountEvent, AccountState> getAggregateRepository() {
    return new AggregateRepositoryImpl<>(accountCommandHandler, accountEventApplier, aggregateFactory, stateFactory);
  }

  @Bean("accountAggregateFacade")
  public AggregateFacade<AccountAggregate, AccountCommand, AccountEvent, AccountState> getAggregateFacade(
      @Qualifier(ACCOUNT_AGGREGATE_REPOSITORY) AggregateRepository aggregateRepository,
      ApplicationMessageBus bus
  ) {
    return new AggregateFacadeImpl<>(aggregateRepository, bus::publishMessage, s -> {});
  }

}

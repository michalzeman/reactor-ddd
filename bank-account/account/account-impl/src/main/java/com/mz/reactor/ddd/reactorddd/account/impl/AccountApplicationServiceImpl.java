package com.mz.reactor.ddd.reactorddd.account.impl;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.reactorddd.account.api.AccountApplicationService;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import com.mz.reactor.ddd.reactorddd.account.domain.command.AccountCommand;
import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import com.mz.reactor.ddd.reactorddd.account.domain.command.DepositMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountCreated;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyWithdrawn;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountApplicationServiceImpl implements AccountApplicationService {

  private static final Log log = LogFactory.getLog(AccountApplicationServiceImpl.class);

  private final AggregateFacade<AccountAggregate, AccountCommand, AccountState> aggregateFacade;

  public AccountApplicationServiceImpl(
      @Qualifier("accountAggregateFacade") AggregateFacade<AccountAggregate, AccountCommand, AccountState> aggregateFacade
  ) {
    this.aggregateFacade = aggregateFacade;
  }

  @Override
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

}

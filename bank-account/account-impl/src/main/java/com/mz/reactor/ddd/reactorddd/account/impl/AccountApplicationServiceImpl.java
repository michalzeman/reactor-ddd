package com.mz.reactor.ddd.reactorddd.account.impl;

import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.account.api.AccountApplicationService;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import com.mz.reactor.ddd.reactorddd.account.domain.command.AccountCommand;
import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import com.mz.reactor.ddd.reactorddd.account.domain.command.DepositMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountCreated;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountEvent;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyWithdrawn;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AccountApplicationServiceImpl implements AccountApplicationService {

  private final AggregateFacade<AccountAggregate, AccountCommand, AccountEvent, AccountState> aggregateFacade;

  public AccountApplicationServiceImpl(
      ApplicationMessageBus bus,
      @Qualifier("accountAggregateFacade") AggregateFacade<AccountAggregate, AccountCommand, AccountEvent, AccountState> aggregateFacade
  ) {
    this.aggregateFacade = aggregateFacade;
  }

  @Override
  public Mono<AccountCreated> execute(CreateAccount cmd) {
    return this.aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId()).cast(AccountCreated.class);
  }

  @Override
  public Mono<MoneyDeposited> execute(DepositMoney cmd) {
    return this.aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId()).cast(MoneyDeposited.class);
  }

  @Override
  public Mono<MoneyWithdrawn> execute(WithdrawMoney cmd) {
    return this.aggregateFacade.executeReturnEvent(cmd, cmd.aggregateId()).cast(MoneyWithdrawn.class);
  }
}

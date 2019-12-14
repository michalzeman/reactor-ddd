package com.mz.reactor.ddd.reactorddd.account.api;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.reactorddd.account.domain.command.AccountCommand;
import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import com.mz.reactor.ddd.reactorddd.account.domain.command.DepositMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.command.WithdrawMoney;
import com.mz.reactor.ddd.reactorddd.account.domain.event.AccountCreated;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyWithdrawn;
import reactor.core.publisher.Mono;

public interface AccountApplicationService {

  Mono<AccountCreated> execute(CreateAccount cmd);

  Mono<MoneyDeposited> execute(DepositMoney cmd);

  Mono<MoneyWithdrawn> execute(WithdrawMoney cmd);

  <R extends DomainEvent> Mono<R> execute(AccountCommand cmd, Class<R> eventType);

}

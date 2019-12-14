package com.mz.reactor.ddd.reactorddd.account.impl;

import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.account.api.AccountQuery;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import com.mz.reactor.ddd.reactorddd.account.wiring.AccountConfiguration;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.ViewRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class AccountQueryImpl implements AccountQuery {

  private final ViewRepository<AccountState> repository;

  public AccountQueryImpl(
      @Qualifier(AccountConfiguration.ACCOUNT_VIEW_REPOSITORY) ViewRepository<AccountState> repository,
      ApplicationMessageBus bus
  ) {
    this.repository = Objects.requireNonNull(repository);
    Objects.requireNonNull(bus).messagesStream()
        .filter(m -> m instanceof AccountState)
        .cast(AccountState.class)
        .subscribe(repository::addView);
  }

  @Override
  public Mono<AccountState> findById(@NonNull String id) {
    return repository.findBy(v -> v.id().equals(id));
  }

  @Override
  public Flux<AccountState> getAll() {
    return repository.findAllBy(v -> true);
  }
}

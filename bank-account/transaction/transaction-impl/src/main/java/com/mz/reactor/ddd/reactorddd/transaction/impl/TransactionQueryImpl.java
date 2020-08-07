package com.mz.reactor.ddd.reactorddd.transaction.impl;

import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.ViewRepository;
import com.mz.reactor.ddd.reactorddd.transaction.api.TransactionQuery;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.mz.reactor.ddd.reactorddd.transaction.wiring.TransactionConfiguration.TRANSACTION_VIEW_REPOSITORY;

@Service
public class TransactionQueryImpl implements TransactionQuery {

  private final ViewRepository<TransactionState> repository;

  public TransactionQueryImpl(
      @Qualifier(TRANSACTION_VIEW_REPOSITORY) ViewRepository<TransactionState> repository,
      ApplicationMessageBus bus
  ) {
    this.repository = Objects.requireNonNull(repository);
    Objects.requireNonNull(bus).messagesStream()
        .filter(m -> m instanceof TransactionState)
        .cast(TransactionState.class)
        .subscribe(repository::addView);
  }

  @Override
  public Mono<TransactionState> findById(@NonNull String id) {
    return repository.findBy(v -> v.id().equals(id));
  }

  @Override
  public Flux<TransactionState> getAll() {
    return repository.findAllBy(v -> true);
  }
}

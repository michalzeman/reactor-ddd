package com.mz.reactor.ddd.reactorddd.persistance.query.impl;

import com.mz.reactor.ddd.common.api.view.DomainView;
import com.mz.reactor.ddd.reactorddd.persistance.query.Query;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.ViewRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class QueryImpl<S extends DomainView> implements Query<S> {

  private final ViewRepository<S> repository;

  private final Predicate<S> getAll = v -> true;

  public QueryImpl(
      ViewRepository<S> repository,
      Supplier<Flux<S>> documentStream
  ) {
    this.repository = requireNonNull(repository, "repository is required");
    requireNonNull(documentStream, "documentStream is required").get()
        .subscribe(repository::addView);
  }

  @Override
  public Mono<S> findById(@NonNull String id) {
    return repository.findBy(view -> view.id().equals(id));
  }

  @Override
  public Flux<S> getAll() {
    return repository.findAllBy(getAll);
  }
}

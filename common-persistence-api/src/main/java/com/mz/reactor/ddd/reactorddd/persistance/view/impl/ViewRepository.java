package com.mz.reactor.ddd.reactorddd.persistance.view.impl;

import com.mz.reactor.ddd.common.api.view.DomainView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

public interface ViewRepository<S extends DomainView> {

  void addView(S view);

  Mono<S> findBy(Predicate<S> query);

  Mono<List<S>> findAllListBy(Predicate<S> query);

  Flux<S> findAllBy(Predicate<S> query);
}

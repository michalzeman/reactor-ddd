package com.mz.reactor.ddd.reactorddd.persistance.query;

import com.mz.reactor.ddd.common.api.view.DomainView;
import com.mz.reactor.ddd.reactorddd.persistance.query.impl.QueryImpl;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.ViewRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public interface Query<S extends DomainView> {

  Mono<S> findById(String id);

  Flux<S> getAll();

  static <S extends DomainView> Query<S> of(ViewRepository<S> viewRepository, Supplier<Flux<S>> documentStream) {
    return new QueryImpl<>(viewRepository, documentStream);
  }
}

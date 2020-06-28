package com.mz.reactor.ddd.reactorddd.persistance.view.impl.impl;

import com.mz.reactor.ddd.common.api.view.DomainView;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.ViewRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class ViewRepositoryImpl<S extends DomainView> implements ViewRepository<S> {

  private final AtomicReference<Map<String, S>> storage = new AtomicReference<>(new HashMap<>());

  UnicastProcessor<S> storageProcessor = UnicastProcessor.create();

  FluxSink<S> sink = storageProcessor.sink();

  public ViewRepositoryImpl() {
    storageProcessor
        .parallel()
        .subscribe(view -> storage.updateAndGet(sm -> {
          sm.put(view.id(), view);
          return sm;
        }));
  }

  @Override
  public void addView(S view) {
    sink.next(view);
  }

  @Override
  public Mono<S> findBy(Predicate<S> query) {
    return findAllBy(query)
        .singleOrEmpty();
  }

  @Override
  public Mono<List<S>> findAllListBy(Predicate<S> query) {
    return findAllBy(query).collectList();
  }

  @Override
  public Flux<S> findAllBy(Predicate<S> query) {
    return Optional.ofNullable(query)
        .map(Flux.fromStream(storage.get().values().stream())::filter)
        .orElse(Flux.empty());
  }
}

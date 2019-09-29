package com.mz.reactor.ddd.reactorddd.persistance.view;

import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class ViewRepositoryImpl<S> implements ViewRepository<S> {

  private Set<S> storage = new LinkedHashSet<>();

  UnicastProcessor<S> storageProcessor = UnicastProcessor.create();

  FluxSink<S> sink = storageProcessor.sink();

  public ViewRepositoryImpl() {
    storageProcessor
        .subscribeOn(Schedulers.single())
        .subscribe(view -> storage.add(view));
  }

  public void addView(S view) {
    sink.next(view);
  }

  @Override
  public Mono<S> findBy(Predicate<S> query) {
    return Mono.just(query)
        .flatMap(q -> storage.stream()
            .filter(q)
            .findFirst()
            .map(Mono::just)
            .orElseGet(Mono::empty)
        );
  }

  @Override
  public Mono<List<S>> findAllByList(Predicate<S> query) {
    return findAllBy(query).collectList();
  }

  @Override
  public Flux<S> findAllBy(Predicate<S> query) {
    return Flux.fromStream(storage.stream()
        .filter(query));
  }
}

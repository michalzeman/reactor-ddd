package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateActor;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateRepository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AggregateRepositoryImpl<A, C extends Command, E extends DomainEvent,S> implements AggregateRepository<A, C, E, S> {

  private final AtomicReference<Map<Id, List<E>>> eventSource = new AtomicReference<>(new HashMap<>());

  private final Cache<Id, AggregateActor<A, C, E>> cache = CacheBuilder.newBuilder()
      .expireAfterAccess(Duration.ofMinutes(10))
      .removalListener((RemovalListener<Id, AggregateActor<A, C, E>>) notification -> notification.getValue().onDestroy())
      .build();


  private final CommandHandler<A, C> commandHandler;

  private final EventApplier<A, E> eventApplier;

  private final Function<Id, A> aggregateFactory;

  private final Function<A, S> stateFactory;

  public AggregateRepositoryImpl(
      CommandHandler<A, C> commandHandler,
      EventApplier<A, E> eventApplier,
      Function<Id, A> aggregateFactory,
      Function<A, S> stateFactory
  ) {
    this.commandHandler = commandHandler;
    this.eventApplier = eventApplier;
    this.aggregateFactory = aggregateFactory;
    this.stateFactory = stateFactory;
  }

  private List<E> persistAll(Id id, List<E> events) {
    eventSource.updateAndGet(esMap -> {
      var eventsToStore = Optional.ofNullable(esMap.get(id))
          .map(es -> Stream.concat(es.stream(), events.stream())
              .sorted(Comparator.comparing(DomainEvent::createdAt))
              .collect(toList()))
          .orElse(events);
      esMap.put(id, eventsToStore);
      return esMap;
    });
    return events;
  }

  private Mono<AggregateActor<A, C, E>> getAggregate(Id id) {
    return Mono.just(id)
        .map(this::getFromCache);
  }

  private AggregateActor<A, C, E> getFromCache(Id id) {
    try {
      return cache.get(id, () ->
          new AggregateActorImpl<>(
              id,
              commandHandler,
              eventApplier,
              aggregateFactory,
              Optional.ofNullable(eventSource.get().get(id)).orElseGet(List::of),
              this::persistAll));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Mono<CommandResult<E>> execute(C cmd, Id aggregateId) {
    return getAggregate(aggregateId)
        .flatMap(a -> a.execute(cmd));
  }

  @Override
  public Mono<S> findById(Id id) {
    return getAggregate(id).flatMap(a -> a.getState(this.stateFactory));
  }

}

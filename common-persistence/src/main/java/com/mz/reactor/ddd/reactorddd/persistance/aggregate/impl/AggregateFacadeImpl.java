package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.Message;
import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateRepository;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;


public class AggregateFacadeImpl<A, C extends Command, E extends DomainEvent ,S> implements AggregateFacade<A, C, E, S> {

  private final AggregateRepository<A, C, E, S> aggregateRepository;
  private final Consumer<Message> publishChanged;
  private final Consumer<S> publishDocument;

  public AggregateFacadeImpl(
      AggregateRepository<A, C, E, S> aggregateRepository,
      Consumer<Message> publishChanged,
      Consumer<S> publishDocument
  ) {
    this.aggregateRepository = aggregateRepository;
    this.publishChanged = publishChanged;
    this.publishDocument = publishDocument;
  }

  @Override
  public Mono<S> execute(C command, String aggregateID) {
    return aggregateRepository.execute(command, new Id(aggregateID))
        .flatMap(processResult(aggregateID));
  }

  @Override
  public Mono<E> executeReturnEvent(C command, String aggregateID) {
    var result = aggregateRepository.execute(command, new Id(aggregateID));
    return result.map(r -> r.events().stream().findAny().get());
  }

  private Function<CommandResult<E>, Mono<S>> processResult(String aggregateId) {
    return result -> {
      switch (result.statusCode()) {
        case OK:
          return aggregateRepository.findById(new Id(aggregateId))
              .doOnSuccess(publishDocument)
              .doOnSuccess(s -> result.events().forEach(publishChanged));
        case FAILED:
          return Mono.error(result.error().orElseGet(() -> new RuntimeException("Generic error")));
        case NOT_MODIFIED:
        default:
          return Mono.empty();
      }
    };
  }

}

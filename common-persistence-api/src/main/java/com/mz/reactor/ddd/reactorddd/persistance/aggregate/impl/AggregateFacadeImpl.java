package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.Message;
import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateFacade;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;


public class AggregateFacadeImpl<A, C extends Command, S> implements AggregateFacade<A, C, S> {

  private static final Log log = LogFactory.getLog(AggregateFacadeImpl.class);

  private final AggregateRepository<A, C, S> aggregateRepository;
  private final Consumer<Message> publishChanged;
  private final Consumer<S> publishDocument;

  public AggregateFacadeImpl(
      AggregateRepository<A, C, S> aggregateRepository,
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
        .flatMap(cr -> processResult(aggregateID, cr))
        .doOnError(error -> log.error("execute -> ", error));
  }

  @Override
  public Mono<? extends DomainEvent> executeReturnEvent(C command, String aggregateID, Class<? extends DomainEvent> eventType) {
    var result = aggregateRepository.execute(command, new Id(aggregateID));
    return result.flatMap(cr -> processResult(aggregateID, eventType, cr))
        .doOnError(error -> log.error("execute -> event type: " + eventType, error));
  }

  @Override
  public Mono<S> findById(String aggregateId) {
    return aggregateRepository.findById(Id.of(aggregateId));
  }

  @Override
  public Mono<S> findByIdIfExists(String aggregateId) {
    return aggregateRepository.findIfExists(Id.of(aggregateId));
  }

  private Mono<? extends DomainEvent> processResult(String aggregateId, Class<? extends DomainEvent> eventType, CommandResult result) {
    switch (result.statusCode()) {
      case OK:
        return publishChanges(aggregateId, result)
            .map(state -> result.events().stream()
                .filter(e -> isInstance(e, eventType))
                .map(eventType::cast)
                .findAny())
            .map(Optional::get);
      case FAILED:
        return onFailed(result);
      case NOT_MODIFIED:
      default:
        return Mono.empty();
    }
  }

  private Mono<S> processResult(String aggregateId, CommandResult result) {
    switch (result.statusCode()) {
      case OK:
        return publishChanges(aggregateId, result);
      case FAILED:
        return onFailed(result);
      case NOT_MODIFIED:
      default:
        return Mono.empty();
    }
  }

  private <T> Mono<T> onFailed(CommandResult result) {
    result.events().forEach(publishChanged);
    return Mono.error(result.error().orElseGet(() -> new RuntimeException("Generic error")));
  }

  private Mono<S> publishChanges(String aggregateId, CommandResult result) {
    return aggregateRepository.findById(new Id(aggregateId))
        .doOnSuccess(publishDocument)
        .doOnSuccess(s -> result.events().forEach(publishChanged));
  }

  protected <E> boolean isInstance(Object obj, Class<E> type) {
    return Optional.ofNullable(type)
        .flatMap(t -> Optional.ofNullable(obj).map(t::isInstance)).orElse(false);
  }

}

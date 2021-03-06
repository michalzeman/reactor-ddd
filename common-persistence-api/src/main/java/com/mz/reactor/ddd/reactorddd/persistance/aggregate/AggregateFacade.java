package com.mz.reactor.ddd.reactorddd.persistance.aggregate;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import reactor.core.publisher.Mono;

/**
 * Aggregate facade contract for operations related with aggregate.
 * @param <A> - Aggregate type
 * @param <C> - Command type
 * @param <S> - State type, representing a state of aggregate
 */
public interface AggregateFacade<A, C extends Command,S> {

  Mono<S> execute(C command, String aggregateID);

  Mono<? extends DomainEvent> executeReturnEvent(C command, String aggregateID, Class<? extends DomainEvent> event);

  Mono<S> findById(String aggregateId);

  Mono<S> findByIdIfExists(String aggregateId);
}

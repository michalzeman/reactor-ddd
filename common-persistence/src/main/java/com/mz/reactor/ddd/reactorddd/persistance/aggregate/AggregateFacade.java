package com.mz.reactor.ddd.reactorddd.persistance.aggregate;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import reactor.core.publisher.Mono;

public interface AggregateFacade<A, C extends Command, E extends DomainEvent,S> {

  Mono<S> execute(C command, String aggregateID);

  Mono<E> executeReturnEvent(C command, String aggregateID);
}

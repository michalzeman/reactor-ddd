package com.mz.reactor.ddd.reactorddd.persistance.aggregate;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import reactor.core.publisher.Mono;

/**
 * {@link AggregateRepository} contract definition.
 * @param <A> - Aggregate type
 * @param <C> - Command type
 * @param <S> - State type, representing a state of aggregate
 */
public interface AggregateRepository<A, C extends Command,S> {

  Mono<CommandResult> execute(C cmd, Id aggregateId);

  Mono<S> findById(Id id);

  Mono<S> findIfExists(Id id);
}

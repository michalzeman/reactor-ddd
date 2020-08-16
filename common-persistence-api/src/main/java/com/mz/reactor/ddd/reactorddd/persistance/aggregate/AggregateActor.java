package com.mz.reactor.ddd.reactorddd.persistance.aggregate;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Contract definition for Aggregate wrapper
 * @param <A> - Aggregate type
 * @param <C> - Command type
 */
public interface AggregateActor<A, C extends Command> {

  <S> Mono<S> getState(Function<A, S> stateFactory);

  void onDestroy();

  Mono<CommandResult> execute(C cmd);

}

package com.mz.reactor.ddd.reactorddd.persistance.aggregate;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface AggregateActor<A, C extends Command> {

  <S> Mono<S> getState(Function<A, S> stateFactory);

  void onDestroy();

  Mono<CommandResult> execute(C cmd);

}

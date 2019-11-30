package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateActor;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AggregateActorImpl<A, C extends Command, E extends DomainEvent> implements AggregateActor<A, C, E> {

  private final Id id;

  private final CommandHandler<A, C> commandHandler;

  private final EventApplier<A, E> eventApplier;

  private final BiFunction<Id, List<E>, List<E>> persistAll;

  private A aggregate;

  private final UnicastProcessor<C> commandProcessor = UnicastProcessor.create();

  private final FluxSink<C> commandSink = commandProcessor.sink();

  private final ReplayProcessor<CommandResult<E>> commandResultReplayProcessor = ReplayProcessor.create();

  private final FluxSink<CommandResult<E>> commandResultSink = commandResultReplayProcessor.sink();

  public AggregateActorImpl(
      Id id,
      CommandHandler<A, C> commandHandler,
      EventApplier<A, E> eventApplier,
      Function<Id, A> aggregateFactory,
      List<E> domainEvents,
      BiFunction<Id, List<E>, List<E>> persistAll
  ) {
    this.id = id;
    this.commandHandler = commandHandler;
    this.eventApplier = eventApplier;
    this.persistAll = persistAll;
    this.aggregate = domainEvents.stream()
        .reduce(
            aggregateFactory.apply(id),
            eventApplier::apply,
            (prevAg, nextAg) -> nextAg
        );
    commandProcessor
        .publishOn(Schedulers.newSingle(String.format("AggregateActor: %s", id)))
        .log()
        .subscribe(this::handleCommand);
  }

  private void handleCommand(C cmd) {
    var commandResult = commandHandler.<E>execute(this.aggregate, cmd);
    this.aggregate = (A) persistAll
        .andThen(events -> events.stream()
            .reduce(this.aggregate, eventApplier::apply, (a1, a2) -> a2))
        .apply(id, commandResult.events());
    commandResultSink.next(commandResult);
  }

  @Override
  public Mono<CommandResult<E>> execute(C cmd) {
    Mono<CommandResult<E>> result = commandResultReplayProcessor.publishOn(Schedulers.elastic())
        .filter(r -> r.commandId().equals(cmd.commandId()))
        .next();
    commandSink.next(cmd);
    return result.timeout(Duration.ofSeconds(10));
  }

  public void onDestroy() {
    commandSink.complete();
    commandProcessor.clear();
    commandProcessor.cancel();

    commandResultSink.complete();
    if (commandResultReplayProcessor.isDisposed()) {
      commandResultReplayProcessor.dispose();
    }
  }

  public <S> Mono<S> getState(Function<A, S> stateFactory) {
    return Mono.just(stateFactory.apply(aggregate));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    var that = (AggregateActorImpl) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

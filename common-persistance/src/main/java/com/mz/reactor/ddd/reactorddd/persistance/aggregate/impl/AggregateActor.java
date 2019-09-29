package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AggregateActor<A, C extends Command> {

  private final Id id;

  private final CommandHandler<A, C> commandHandler;

  private final EventApplier<A, DomainEvent> eventApplier;

  private final BiFunction<Id, List<DomainEvent>, List<DomainEvent>> persistAll;

  private A aggregate;

  private final UnicastProcessor<C> commandProcessor = UnicastProcessor.create();

  private final FluxSink<C> commandSink = commandProcessor.sink();

  private final ReplayProcessor<CommandResult> commandResultReplayProcessor = ReplayProcessor.create();

  private final FluxSink<CommandResult> commandResultSink = commandResultReplayProcessor.sink();

  public AggregateActor(
      Id id,
      CommandHandler<A, C> commandHandler,
      EventApplier<A, DomainEvent> eventApplier,
      Function<Id, A> aggregateFactory,
      List<DomainEvent> domainEvents,
      BiFunction<Id, List<DomainEvent>, List<DomainEvent>> persistAll
  ) {
    this.id = id;
    this.commandHandler = commandHandler;
    this.eventApplier = eventApplier;
    this.persistAll = persistAll;
    this.aggregate = domainEvents.stream()
        .reduce(
            aggregateFactory.apply(id),
            eventApplier::apply,
            (a1, a2) -> a2
        );
    commandProcessor
        .publishOn(Schedulers.newSingle(String.format("AggregateActor: %s", id)))
        .log()
        .subscribe(this::handleCommand);
  }

  private void handleCommand(C cmd) {
    var commandResult = commandHandler.execute(this.aggregate, cmd);
    this.aggregate = persistAll
        .andThen(events -> events.stream()
            .reduce(this.aggregate, eventApplier::apply, (a1, a2) -> a2))
        .apply(id, commandResult.events());
    commandResultSink.next(commandResult);
  }

  public Mono<CommandResult> execute(C cmd) {
    Mono<CommandResult> result = commandResultReplayProcessor.publishOn(Schedulers.elastic())
        .filter(r -> r.commandId().equals(cmd.commandId()))
        .cast(CommandResult.class)
        .next();
    commandSink.next(cmd);
    return result;
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

  public <S> S getState(Function<A, S> stateFactory) {
    return stateFactory.apply(aggregate);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AggregateActor that = (AggregateActor) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

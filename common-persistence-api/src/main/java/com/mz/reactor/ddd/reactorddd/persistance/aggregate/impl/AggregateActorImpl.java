package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventHandler;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import com.mz.reactor.ddd.reactorddd.persistance.aggregate.AggregateActor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class AggregateActorImpl<A, C extends Command> implements AggregateActor<A, C> {

  private static final Log log = LogFactory.getLog(AggregateActorImpl.class);

  private final Id id;

  private final CommandHandler<A, C> commandHandler;

  private final EventHandler<A> eventHandler;

  private final BiFunction<Id, List<? extends DomainEvent>, List<? extends DomainEvent>> persistAll;

  private A aggregate;

  private final Sinks.Many<C> commandProcessor = Sinks.many().replay().all();

  private final Sinks.Many<CommandResult> commandResultReplayProcessor = Sinks.many().replay().all();

  public AggregateActorImpl(
      Id id,
      CommandHandler<A, C> commandHandler,
      EventHandler<A> eventHandler,
      Function<Id, A> aggregateFactory,
      List<? extends DomainEvent> domainEvents,
      BiFunction<Id, List<? extends DomainEvent>, List<? extends DomainEvent>> persistAll
  ) {
    this.id = id;
    this.commandHandler = commandHandler;
    this.eventHandler = eventHandler;
    this.persistAll = persistAll;
    this.aggregate = domainEvents.stream()
        .reduce(
            aggregateFactory.apply(id),
            eventHandler::apply,
            (prevAg, nextAg) -> nextAg
        );
    commandProcessor.asFlux()
        .publishOn(Schedulers.newSingle(String.format("AggregateActor: %s", id)))
        .log()
        .doOnError(error -> log.error("handleCommand -> ", error))
        .subscribe(this::handleCommand);
  }

  private void handleCommand(C cmd) {
    var commandResult = commandHandler.execute(this.aggregate, cmd);
    this.aggregate = persistAll
        .andThen(events -> events.stream()
            .reduce(this.aggregate, eventHandler::apply, (a1, a2) -> a2))
        .apply(id, commandResult.events());
    commandResultReplayProcessor.emitNext(commandResult, FAIL_FAST);
  }

  @Override
  public Mono<CommandResult> execute(C cmd) {
    Mono<CommandResult> result = commandResultReplayProcessor.asFlux().publishOn(Schedulers.boundedElastic())
        .filter(r -> r.commandId().equals(cmd.commandId()))
        .next();
    commandProcessor.emitNext(cmd, FAIL_FAST);
    return result.timeout(Duration.ofSeconds(10));
  }

  public void onDestroy() {
    commandProcessor.emitComplete(FAIL_FAST);
    commandResultReplayProcessor.emitComplete(FAIL_FAST);
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

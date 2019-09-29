package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.command.Command;
import com.mz.reactor.ddd.common.api.command.CommandHandler;
import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.common.api.event.EventApplier;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

class AggregateActorTest {

  private final CommandHandler<TestAggregate, TestAggregateCommand> commandHandler = new CommandHandler<TestAggregate, TestAggregateCommand>() {
    @Override
    public CommandResult execute(TestAggregate aggregate, TestAggregateCommand command) {
      return CommandResult.builder()
          .commandId(command.commandId())
          .statusCode(CommandResult.StatusCode.OK)
          .addEvents()
          .build();
    }
  };

  private final EventApplier<TestAggregate, DomainEvent> eventApplier = new EventApplier<TestAggregate, DomainEvent>() {
    @Override
    public TestAggregate apply(TestAggregate aggregate, DomainEvent event) {
      return new TestAggregate();
    }
  };

  private final Function<Id, TestAggregate> aggregateFactory = new Function<Id, TestAggregate>() {
    @Override
    public TestAggregate apply(Id id) {
      return new TestAggregate();
    }
  };

  private final BiFunction<Id, List<DomainEvent>, List<DomainEvent>> persistAll = (id, events) -> events;

  @Test
  void execute() {
    var commandId = UUID.randomUUID().toString();
    var command = new TestAggregateCommand(commandId);

    var subject = new AggregateActor<TestAggregate, TestAggregateCommand>(
        new Id(UUID.randomUUID().toString()),
        commandHandler,
        eventApplier,
        aggregateFactory,
        List.of(),
        persistAll
    );

    Mono<CommandResult> result = subject.execute(command);
    StepVerifier.create(result)
        .expectNextMatches(r -> r.commandId().equals(commandId))
        .expectComplete().verify();
  }

  @Test
  void onDestroy() {
    var subject = new AggregateActor<TestAggregate, TestAggregateCommand>(
        new Id(UUID.randomUUID().toString()),
        commandHandler,
        eventApplier,
        aggregateFactory,
        List.of(),
        persistAll
    );

    subject.onDestroy();
    StepVerifier.create(subject.execute(new TestAggregateCommand(null)))
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void executeParallel() {
    var subject = new AggregateActor<TestAggregate, TestAggregateCommand>(
        new Id(UUID.randomUUID().toString()),
        commandHandler,
        eventApplier,
        aggregateFactory,
        List.of(),
        persistAll
    );

    var source = Flux.range(0, 100)
        .parallel(10)
        .map(i -> new TestAggregateCommand("Command:" + i))
        .flatMap(subject::execute);

    StepVerifier.create(source)
        .expectNextCount(100)
        .verifyComplete();
  }

  private static class TestAggregate {

  }

  private static class TestAggregateCommand implements Command {

    private final String commandId;

    public TestAggregateCommand(String commandId) {
      this.commandId = Optional.ofNullable(commandId).orElseGet(() -> UUID.randomUUID().toString());
    }

    @Override
    public String commandId() {
      return commandId;
    }

    @Override
    public Optional<String> correlationId() {
      return Optional.empty();
    }
  }

  private static class TestAggregateEvent implements DomainEvent {

    @Override
    public Optional<String> correlationId() {
      return Optional.empty();
    }
  }
}
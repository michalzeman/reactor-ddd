package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.common.api.valueobject.Id;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

class AggregateActorTest {

  @Test
  void execute() {
    var commandId = UUID.randomUUID().toString();
    var command = TestAggregateCommand.newBuilder()
        .withCommandId(commandId)
        .withValue(10)
        .build();

    var subject = new AggregateActorImpl<TestAggregate, TestAggregateCommand>(
        new Id(UUID.randomUUID().toString()),
        TestFunctions.FN.commandHandler,
        TestFunctions.FN.eventHandler,
        TestFunctions.FN.aggregateFactory,
        List.of(),
        TestFunctions.FN.persistAll
    );

    Mono<CommandResult> result = subject.execute(command);
    StepVerifier.create(result)
        .expectNextMatches(r ->
            r.commandId().equals(commandId) && r.statusCode().equals(CommandResult.StatusCode.OK))
        .expectComplete().verify();
  }

  @Test
  void onDestroy() {
    var subject = new AggregateActorImpl<TestAggregate, TestAggregateCommand>(
        new Id(UUID.randomUUID().toString()),
        TestFunctions.FN.commandHandler,
        TestFunctions.FN.eventHandler,
        TestFunctions.FN.aggregateFactory,
        List.of(),
        TestFunctions.FN.persistAll
    );

    subject.onDestroy();
    StepVerifier.create(subject.execute(TestAggregateCommand.newBuilder()
        .withCommandId(String.format("comman: %s", 1))
        .withValue(1)
        .build()))
        .expectNextCount(0)
        .verifyComplete();
  }

  @Test
  void executeParallel() {
    var subject = new AggregateActorImpl<TestAggregate, TestAggregateCommand>(
        new Id(UUID.randomUUID().toString()),
        TestFunctions.FN.commandHandler,
        TestFunctions.FN.eventHandler,
        TestFunctions.FN.aggregateFactory,
        List.of(),
        TestFunctions.FN.persistAll
    );

    var source = Flux.range(0, 100)
        .parallel(10)
        .map(i -> TestAggregateCommand.newBuilder()
            .withCommandId(String.format("comman: %s", i))
            .withValue(i)
            .build())
        .flatMap(subject::execute);

    StepVerifier.create(source)
        .expectNextCount(100)
        .verifyComplete();
  }

  @Test
  public void testRecoveryState() {
    var subject = new AggregateActorImpl<TestAggregate, TestAggregateCommand>(
        new Id(UUID.randomUUID().toString()),
        TestFunctions.FN.commandHandler,
        TestFunctions.FN.eventHandler,
        TestFunctions.FN.aggregateFactory,
        List.of(TestAggregateEvent.newBuilder()
            .withAmount(10L)
            .build(), TestAggregateEvent.newBuilder()
            .withAmount(12L)
            .build()),
        TestFunctions.FN.persistAll
    );

    Mono<Long> source = subject.getState(TestAggregate::getValue);
    StepVerifier.create(source)
        .expectNext(22L)
        .verifyComplete();
  }

  @Test
  public void test_RecoveryStateAndExecuteCommand() {
    var subject = new AggregateActorImpl<TestAggregate, TestAggregateCommand>(
        new Id(UUID.randomUUID().toString()),
        TestFunctions.FN.commandHandler,
        TestFunctions.FN.eventHandler,
        TestFunctions.FN.aggregateFactory,
        List.of(TestAggregateEvent.newBuilder()
            .withAmount(10l)
            .build(), TestAggregateEvent.newBuilder()
            .withAmount(12l)
            .build()),
        TestFunctions.FN.persistAll
    );

    var cmd = TestAggregateCommand.newBuilder()
        .withCommandId(String.format("comman: %s", 10))
        .withValue(10)
        .build();

    subject.execute(cmd).block();

    Mono<Long> source = subject.getState(a -> a.getValue());
    StepVerifier.create(source)
        .expectNext(32L)
        .verifyComplete();
  }

}
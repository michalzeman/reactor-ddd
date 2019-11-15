package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.valueobject.Id;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AggregateRepositoryImplTest {

  private final Function<TestAggregate, Tuple2<Id, Long>> getState = a -> Tuples.of(a.getId(), a.getValue());

  AggregateRepositoryImpl<TestAggregate, TestAggregateCommand, Tuple2<Id, Long>> subject =
      new AggregateRepositoryImpl<>(
          TestFunctions.FN.commandHandler,
          TestFunctions.FN.eventApplier,
          TestFunctions.FN.aggregateFactory,
          getState
      );

  @Test
  void findByIdParallel() {
    var source = Flux.range(0, 100)
        .parallel(10)
        .flatMap(i -> {
          var cmd = TestAggregateCommand.newBuilder()
              .withCommandId(String.format("comman: %s", i))
              .withValue(i)
              .build();
          return subject.execute(cmd, new Id(String.valueOf(i)));
        });

    StepVerifier.create(source)
        .expectNextCount(100)
        .verifyComplete();
  }

  @Test
  void findById() {
    var cmdId = UUID.randomUUID().toString();
    var cmd = TestAggregateCommand.newBuilder()
        .withCommandId(cmdId)
        .withValue(10)
        .build();

    var id1 = new Id(UUID.randomUUID().toString());
    var id2 = new Id(UUID.randomUUID().toString());


    subject.execute(cmd, id1).block();
    subject.execute(cmd, id2).block();
    subject.execute(cmd, id1).block();
    subject.execute(cmd, id2).block();
    subject.execute(cmd, id2).block();
    subject.execute(cmd, id2).block();
    subject.execute(cmd, id1).block();
    subject.execute(cmd, id1).delayElement(Duration.ofSeconds(10)).block();
    subject.execute(cmd, id1).block();
    assertThat(subject.findById(id1).block().getT2()).isEqualTo(50);
    assertThat(subject.findById(id2).block().getT2()).isEqualTo(40);
  }
}
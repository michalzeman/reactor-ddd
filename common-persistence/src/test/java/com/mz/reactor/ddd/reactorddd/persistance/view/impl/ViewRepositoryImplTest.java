package com.mz.reactor.ddd.reactorddd.persistance.view.impl;

import com.mz.reactor.ddd.reactorddd.persistance.model.TestView;
import com.mz.reactor.ddd.reactorddd.persistance.view.impl.impl.ViewRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.function.Predicate;

class ViewRepositoryImplTest {

  ViewRepository<TestView> subject;

  private final String id1 = UUID.randomUUID().toString();
  private final String value1 = UUID.randomUUID().toString();

  private final String id2 = UUID.randomUUID().toString();
  private final String value2 = UUID.randomUUID().toString();

  @BeforeEach
  public void before() {
    subject = new ViewRepositoryImpl<>();
    subject.addView(TestView.newBuilder().withId(id1).withValue(value1).build());
    subject.addView(TestView.newBuilder().withId(id2).withValue(value2).build());

    Flux.range(0, 1000000)
        .publishOn(Schedulers.elastic())
        .map(String::valueOf)
        .doOnNext(v -> subject.addView(TestView.newBuilder().withId(v).withValue(v).build()))
        .collectList().block();

  }

  @Test
  void findAllBy_predictionIsForAll_isReturnedAll() {
    var source = subject.findAllBy(v -> true);
    StepVerifier.create(source)
        .expectNextCount(1000002)
        .verifyComplete();
  }

  @Test
  void findAllBy_predictionForTwo_isReturnedTwo() {
    Predicate<TestView> prediction1 = v -> id1.equals(v.getId());
    Predicate<TestView> prediction2 = v -> id2.equals(v.getId());
    var source = subject.findAllBy(prediction1.or(prediction2));
    StepVerifier.create(source)
        .expectNextCount(2)
        .verifyComplete();
  }

  @Test
  void findAllListBy_predictionForTwo_isReturnedTwo() {
    Predicate<TestView> prediction1 = v -> id1.equals(v.getId());
    Predicate<TestView> prediction2 = v -> id2.equals(v.getId());
    var source = subject.findAllListBy(prediction1.or(prediction2));
    StepVerifier.create(source)
        .expectNextMatches(list -> list.size() == 2)
        .verifyComplete();
  }

  @Test
  void findBy_filteredById_isReturnedOne() {
    Predicate<TestView> prediction1 = v -> id1.equals(v.getId());
    var source = subject.findBy(prediction1);
    StepVerifier.create(source)
        .expectNextMatches(item -> item.getId().equals(id1))
        .verifyComplete();
  }
}
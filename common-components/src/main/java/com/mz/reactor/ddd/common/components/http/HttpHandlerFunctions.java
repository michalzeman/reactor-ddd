package com.mz.reactor.ddd.common.components.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public enum HttpHandlerFunctions {
  FN;

  public <T> Function<String, Mono<T>> deserializeJsonString(@Nonnull Class<T> clazz, @Nonnull Scheduler scheduler) {
    return value -> Mono.fromCallable(() -> {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new Jdk8Module());
      return mapper.readValue(value, clazz);
    }).subscribeOn(scheduler);
  }

  public <T> Function<String, Mono<T>> deserializeJsonString(@Nonnull Class<T> clazz) {
    return value -> Mono.fromCallable(() -> {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new Jdk8Module());
      return mapper.readValue(value, clazz);
    }).subscribeOn(Schedulers.elastic());
  }

  public <E extends Throwable> Mono<ServerResponse> onError(E e, ServerRequest req, Consumer<E> logger) {
    return Mono.fromCallable(() -> {
      logger.accept(e);
      return ErrorMessage.builder().error(e.getMessage()).build();
    }).flatMap(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(fromObject(error)));
  }
}

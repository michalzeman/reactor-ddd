package com.mz.reactor.ddd.common.components.http;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

public final class HttpHandlers {

  private HttpHandlers() {}

  public static <T> Function<String, Mono<T>> deserializeJsonString(
      @Nonnull Class<T> clazz,
      @Nonnull Scheduler scheduler,
      @Nonnull ObjectMapper mapper
  ) {
    return value -> Mono.fromCallable(() -> mapper.readValue(value, clazz)).subscribeOn(scheduler);
  }

  public static <T> Function<String, Mono<T>> deserializeJsonString(@Nonnull Class<T> clazz, @Nonnull ObjectMapper mapper) {
    return value -> Mono.fromCallable(() -> mapper.readValue(value, clazz)).subscribeOn(Schedulers.elastic());
  }

  public static <E extends Throwable> Mono<ServerResponse> onError(E e, ServerRequest req, Consumer<E> logger) {
    return Mono.fromCallable(() -> {
      logger.accept(e);
      return ErrorMessage.builder().error(e.getMessage()).build();
    }).flatMap(error -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(fromValue(error)));
  }
}

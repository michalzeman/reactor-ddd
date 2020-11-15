package com.mz.reactor.ddd.common.components.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static com.mz.reactor.ddd.common.components.http.HttpHandlers.deserializeJsonString;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

public interface HttpHandler {

  RouterFunction<ServerResponse> route();

  ObjectMapper mapper();

  default <T> Mono<T> bodyToMono(ServerRequest request, Class<T> clazz, Scheduler scheduler) {
    return request.bodyToMono(String.class)
        .flatMap(deserializeJsonString(clazz, scheduler, mapper()));
  }

  default <T> Mono<T> bodyToMono(ServerRequest request, Class<T> clazz) {
    return request.bodyToMono(String.class)
        .flatMap(deserializeJsonString(clazz, mapper()));
  }

  default <T> Mono<ServerResponse> mapToResponse(T result) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(fromValue(result));
  }

}

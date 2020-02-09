package com.mz.reactor.ddd.common.components.http;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

public interface HttpHandler {

  RouterFunction<ServerResponse> route();

  default <T> Mono<T> bodyToMono(ServerRequest request, Class<T> clazz, Scheduler scheduler) {
    return request.bodyToMono(String.class)
        .flatMap(HttpHandlerFunctions.FN.deserializeJsonString(clazz, scheduler));
  }

  default <T> Mono<T> bodyToMono(ServerRequest request, Class<T> clazz) {
    return request.bodyToMono(String.class)
        .flatMap(HttpHandlerFunctions.FN.deserializeJsonString(clazz));
  }

  default <T> Mono<ServerResponse> mapToResponse(T result) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(fromObject(result));
  }

}

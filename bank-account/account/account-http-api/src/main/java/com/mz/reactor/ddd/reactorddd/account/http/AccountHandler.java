package com.mz.reactor.ddd.reactorddd.account.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mz.reactor.ddd.common.components.http.HttpHandler;
import com.mz.reactor.ddd.reactorddd.account.api.AccountApplicationService;
import com.mz.reactor.ddd.reactorddd.account.api.AccountQuery;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import com.mz.reactor.ddd.reactorddd.account.http.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class AccountHandler implements HttpHandler {

  private final ObjectMapper mapper;

  private final AccountApplicationService service;

  private final AccountQuery accountQuery;

  private final Scheduler scheduler;

  public AccountHandler(
      AccountApplicationService service,
      AccountQuery accountQuery,
      @Qualifier("JsonDesScheduler") Scheduler scheduler,
      ObjectMapper mapper
  ) {
    this.mapper = mapper;
    this.service = service;
    this.accountQuery = accountQuery;
    this.scheduler = scheduler;
  }

  public Mono<ServerResponse> getById(ServerRequest request) {
    return accountQuery.findById(request.pathVariable("id"))
        .flatMap(this::mapToResponse);
  }

  public Mono<ServerResponse> getAll(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(accountQuery.getAll(), AccountState.class);
  }

  public Mono<ServerResponse> createAccount(ServerRequest request) {
    return bodyToMono(request, CreateAccountRequest.class, scheduler)
        .map(CreateAccountRequest::payload)
        .flatMap(service::execute)
        .map(CreateAccountResponse::from)
        .flatMap(this::mapToResponse);
  }

  public Mono<ServerResponse> depositMoney(ServerRequest request) {
    return bodyToMono(request, DepositMoneyRequest.class, scheduler)
        .map(DepositMoneyRequest::payload)
        .flatMap(service::execute)
        .map(DepositMoneyResponse::from)
        .flatMap(this::mapToResponse);
  }

  public Mono<ServerResponse> withdrawMoney(ServerRequest request) {
    return bodyToMono(request, WithdrawMoneyRequest.class, scheduler)
        .map(WithdrawMoneyRequest::payload)
        .flatMap(service::execute)
        .map(WithdrawMoneyResponse::from)
        .flatMap(this::mapToResponse);
  }

  public RouterFunction<ServerResponse> route() {
    var route = RouterFunctions
        .route(POST("").and(accept(MediaType.APPLICATION_JSON)), this::createAccount)
        .andRoute(GET("").and(accept(MediaType.APPLICATION_JSON)), this::getAll)
        .andRoute(GET("/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getById)
        .andRoute(PUT("/moneys/withdraw").and(accept(MediaType.APPLICATION_JSON)), this::withdrawMoney)
        .andRoute(PUT("/moneys/deposit").and(accept(MediaType.APPLICATION_JSON)), this::depositMoney);

    return RouterFunctions.route()
        .nest(path("/accounts"), () -> route)
        .build();
  }

  @Override
  public ObjectMapper mapper() {
    return mapper;
  }

}

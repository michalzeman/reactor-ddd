package com.mz.reactor.ddd.reactorddd.account.api;

import com.mz.reactor.ddd.common.components.http.HttpHandler;
import com.mz.reactor.ddd.reactorddd.account.api.model.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class AccountHandler implements HttpHandler {

  private final AccountApplicationService service;

  public AccountHandler(AccountApplicationService service) {
    this.service = service;
  }

  public Mono<ServerResponse> createAccount(ServerRequest request) {
    return request
        .bodyToMono(CreateAccountRequest.class)
        .map(CreateAccountRequest::payload)
        .flatMap(service::execute)
        .map(CreateAccountResponse::from)
        .flatMap(this::mapToResponse);
  }

  public Mono<ServerResponse> depositMoney(ServerRequest request) {
    return request
        .bodyToMono(DepositMoneyRequest.class)
        .map(DepositMoneyRequest::payload)
        .flatMap(service::execute)
        .map(DepositMoneyResponse::from)
        .flatMap(this::mapToResponse);
  }

  public Mono<ServerResponse> withdrawMoney(ServerRequest request) {
    return request
        .bodyToMono(WithdrawMoneyRequest.class)
        .map(WithdrawMoneyRequest::payload)
        .flatMap(service::execute)
        .map(WithdrawMoneyResponse::from)
        .flatMap(this::mapToResponse);
  }

  public RouterFunction<ServerResponse> route() {
    var route = RouterFunctions
        .route(POST("").and(accept(MediaType.APPLICATION_JSON_UTF8)), this::createAccount)
        .andRoute(PUT("/moneys/withdraw").and(accept(MediaType.APPLICATION_JSON_UTF8)), this::withdrawMoney)
        .andRoute(PUT("/moneys/deposit").and(accept(MediaType.APPLICATION_JSON_UTF8)), this::withdrawMoney);

    return RouterFunctions.route()
        .nest(path("/accounts"), () -> route)
        .build();
  }

}

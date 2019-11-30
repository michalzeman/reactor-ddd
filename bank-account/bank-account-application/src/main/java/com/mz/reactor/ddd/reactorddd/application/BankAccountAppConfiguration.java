package com.mz.reactor.ddd.reactorddd.application;

import com.mz.reactor.ddd.common.components.http.HttpErrorHandler;
import com.mz.reactor.ddd.reactorddd.account.api.AccountHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@ComponentScan(basePackages = {"com.mz.reactor.ddd.*"})
@Import({com.mz.reactor.ddd.reactorddd.account.wiring.AccountConfiguration.class})
public class BankAccountAppConfiguration {

  @Bean
  public RouterFunction<ServerResponse> statisticRoute(AccountHandler handler) {
    return RouterFunctions.route()
        .add(handler.route())
        .GET("/health", req -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just("Tick"), String.class))
        .onError(Throwable.class, HttpErrorHandler.FN::onError)
        .build();
  }

}

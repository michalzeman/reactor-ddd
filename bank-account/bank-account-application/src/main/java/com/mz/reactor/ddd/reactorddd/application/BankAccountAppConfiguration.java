package com.mz.reactor.ddd.reactorddd.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mz.reactor.ddd.common.components.http.HttpHandlerFunctions;
import com.mz.reactor.ddd.reactorddd.account.http.AccountHandler;
import com.mz.reactor.ddd.reactorddd.transaction.api.TransactionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Configuration
@ComponentScan(basePackages = {"com.mz.reactor.ddd.*"})
public class BankAccountAppConfiguration {

  private static final Log log = LogFactory.getLog(BankAccountAppConfiguration.class);

  @Bean("JsonDesScheduler")
  public Scheduler jsonDesScheduler() {
    return Schedulers.fromExecutor(Executors.newFixedThreadPool(5));
  }

  @Bean
  public RouterFunction<ServerResponse> statisticRoute(AccountHandler accountHandler, TransactionHandler transactionHandler) {
    return RouterFunctions.route()
        .add(accountHandler.route())
        .add(transactionHandler.route())
        .GET("/health", req -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just("Tick"), String.class))
        .onError(Throwable.class,
            (throwable, serverRequest) -> HttpHandlerFunctions.FN.onError(throwable, serverRequest, error -> log.error("Error: ", error)))
        .build();
  }

  @Bean
  @Primary
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
    ObjectMapper objectMapper = builder.build();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }

}

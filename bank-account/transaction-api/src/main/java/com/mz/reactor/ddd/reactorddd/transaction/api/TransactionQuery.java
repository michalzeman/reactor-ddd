package com.mz.reactor.ddd.reactorddd.transaction.api;

import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionQuery {
  Mono<TransactionState> findById(String id);

  Flux<TransactionState> getAll();
}

package com.mz.reactor.ddd.reactorddd.account.api;

import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountQuery {
  Mono<AccountState> findById(String id);

  Flux<AccountState> getAll();
}

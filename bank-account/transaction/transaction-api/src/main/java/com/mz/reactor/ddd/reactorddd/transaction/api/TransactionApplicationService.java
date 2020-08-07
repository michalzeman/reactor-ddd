package com.mz.reactor.ddd.reactorddd.transaction.api;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.FinishTransaction;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.TransactionCommand;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFinished;
import reactor.core.publisher.Mono;

public interface TransactionApplicationService {

  Mono<TransactionCreated> execute(CreateTransaction createTransaction);

  Mono<TransactionFinished> execute(FinishTransaction cmd);

  <R extends DomainEvent> Mono<R> execute(TransactionCommand cmd, Class<R> eventType);
}

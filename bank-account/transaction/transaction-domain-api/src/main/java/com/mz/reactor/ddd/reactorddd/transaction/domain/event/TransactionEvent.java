package com.mz.reactor.ddd.reactorddd.transaction.domain.event;

import com.mz.reactor.ddd.common.api.event.DomainEvent;

public interface TransactionEvent extends DomainEvent {

  String aggregateId();

}

package com.mz.reactor.ddd.reactorddd.account.domain.event;

import com.mz.reactor.ddd.common.api.event.DomainEvent;

public interface AccountEvent extends DomainEvent {

  String aggregateId();


}

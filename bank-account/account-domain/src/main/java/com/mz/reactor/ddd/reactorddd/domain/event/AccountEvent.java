package com.mz.reactor.ddd.reactorddd.domain.event;

import com.mz.reactor.ddd.common.api.event.DomainEvent;
import org.immutables.value.Value;

public interface AccountEvent extends DomainEvent {

  String aggregateId();


}

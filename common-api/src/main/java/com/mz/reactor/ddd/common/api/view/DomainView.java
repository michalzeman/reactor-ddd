package com.mz.reactor.ddd.common.api.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mz.reactor.ddd.common.api.event.DomainEvent;

public interface DomainView extends DomainEvent {
  @JsonIgnore
  String id();
}

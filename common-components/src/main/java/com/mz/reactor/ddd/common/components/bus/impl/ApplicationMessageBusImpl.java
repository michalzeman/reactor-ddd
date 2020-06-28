package com.mz.reactor.ddd.common.components.bus.impl;

import com.mz.reactor.ddd.common.api.Message;
import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Service
public class ApplicationMessageBusImpl implements ApplicationMessageBus {

  private static final Log log = LogFactory.getLog(ApplicationMessageBusImpl.class);

  private final DirectProcessor<Message> messages = DirectProcessor.create();

  private final FluxSink<Message> messagesSink = messages.sink();

  @Override
  public <M extends Message> void publishMessage(M message) {
    log.info(String.format("publishMessage -> messageBusId: %s, message: %s",this.hashCode(), message));
    Optional.ofNullable(message).ifPresent(messagesSink::next);
  }

  @Override
  public Flux<Message> messagesStream() {
    log.info("messagesStream -> " + this.hashCode());
    return messages.publishOn(Schedulers.parallel());
  }

}

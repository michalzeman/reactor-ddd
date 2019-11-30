package com.mz.reactor.ddd.common.components.bus.impl;

import com.mz.reactor.ddd.common.api.Message;
import com.mz.reactor.ddd.common.components.bus.ApplicationMessageBus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Service
public class ApplicationMessageBusImpl implements ApplicationMessageBus {

  private final ReplayProcessor<Message> messages = ReplayProcessor.create(1);

  private final FluxSink<Message> messagesSink = messages.sink();

  @Override
  public <M extends Message> void publishMessage(M message) {
    Optional.ofNullable(message).ifPresent(messagesSink::next);
  }

  @Override
  public Flux<Message> messagesStream() {
    return messages.publishOn(Schedulers.parallel());
  }

}

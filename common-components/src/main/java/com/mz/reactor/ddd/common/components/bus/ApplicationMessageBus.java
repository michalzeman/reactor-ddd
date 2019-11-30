package com.mz.reactor.ddd.common.components.bus;

import com.mz.reactor.ddd.common.api.Message;
import reactor.core.publisher.Flux;

public interface ApplicationMessageBus {

  /**
   * Publish message into the message stream
   * @param message {@link Message} it could be event, command ...
   * @param <M> generic type
   */
  <M extends Message> void publishMessage(M message);

  /**
   * Messages stream where is possible to subscribe and consume some king of messages
   * @return {@link Flux<Message>}
   */
  Flux<Message> messagesStream();
}

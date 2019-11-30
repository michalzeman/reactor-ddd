package com.mz.reactor.ddd.reactorddd.persistance.aggregate.impl;

import com.mz.reactor.ddd.common.api.event.DomainEvent;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class TestAggregateEvent implements DomainEvent {

  private String correlationId;

  private Long amount;

  private String eventId;

  private final Instant createdAt;

  private TestAggregateEvent(Builder builder) {
    correlationId = builder.correlationId;
    amount = builder.amount;
    eventId = builder.eventId;
    createdAt = builder.createdAt;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(TestAggregateEvent copy) {
    Builder builder = new Builder();
    builder.correlationId = copy.getCorrelationId();
    builder.amount = copy.getAmount();
    builder.eventId = copy.getEventId();
    builder.createdAt = copy.createdAt();
    return builder;
  }

  @Override
  public Optional<String> correlationId() {
    return Optional.ofNullable(correlationId);
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public Long getAmount() {
    return amount;
  }

  public String getEventId() {
    return eventId;
  }

  /**
   * {@code TestAggregateEvent} builder static inner class.
   */
  public static final class Builder {
    private String correlationId;
    private Long amount;
    private String eventId;
    private Instant createdAt = Instant.now();

    private Builder() {
    }

    /**
     * Sets the {@code correlationId} and returns a reference to this Builder so that the methods can be chained together.
     *
     * @param val the {@code correlationId} to set
     * @return a reference to this Builder
     */
    public Builder withCorrelationId(String val) {
      correlationId = val;
      return this;
    }

    /**
     * Sets the {@code amount} and returns a reference to this Builder so that the methods can be chained together.
     *
     * @param val the {@code amount} to set
     * @return a reference to this Builder
     */
    public Builder withAmount(Long val) {
      amount = val;
      return this;
    }

    /**
     * Sets the {@code eventId} and returns a reference to this Builder so that the methods can be chained together.
     *
     * @param val the {@code eventId} to set
     * @return a reference to this Builder
     */
    public Builder withEventId(String val) {
      eventId = val;
      return this;
    }

    /**
     * Sets the {@code createdAt} and returns a reference to this Builder so that the methods can be chained together.
     *
     * @param val the {@code createdAt} to set
     * @return a reference to this Builder
     */
    public Builder withCreatedAt(Instant val) {
      createdAt = val;
      return this;
    }

    /**
     * Returns a {@code TestAggregateEvent} built from the parameters previously set.
     *
     * @return a {@code TestAggregateEvent} built with parameters of this {@code TestAggregateEvent.Builder}
     */
    public TestAggregateEvent build() {
      return new TestAggregateEvent(this);
    }
  }
}

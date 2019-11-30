package com.mz.reactor.ddd.reactorddd.persistance.model;

import com.mz.reactor.ddd.common.api.view.DomainView;

public class TestView implements DomainView {
  private final String id;

  private final String value;

  private TestView(Builder builder) {
    id = builder.id;
    value = builder.value;
  }

  public String getId() {
    return id;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String id() {
    return id;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(TestView copy) {
    Builder builder = new Builder();
    builder.id = copy.getId();
    builder.value = copy.getValue();
    return builder;
  }

  /**
   * {@code TestView} builder static inner class.
   */
  public static final class Builder {
    private String id;
    private String value;

    private Builder() {
    }

    /**
     * Sets the {@code id} and returns a reference to this Builder so that the methods can be chained together.
     *
     * @param val the {@code id} to set
     * @return a reference to this Builder
     */
    public Builder withId(String val) {
      id = val;
      return this;
    }

    /**
     * Sets the {@code value} and returns a reference to this Builder so that the methods can be chained together.
     *
     * @param val the {@code value} to set
     * @return a reference to this Builder
     */
    public Builder withValue(String val) {
      value = val;
      return this;
    }

    /**
     * Returns a {@code TestView} built from the parameters previously set.
     *
     * @return a {@code TestView} built with parameters of this {@code TestView.Builder}
     */
    public TestView build() {
      return new TestView(this);
    }
  }
}

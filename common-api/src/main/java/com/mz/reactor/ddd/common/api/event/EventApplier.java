package com.mz.reactor.ddd.common.api.event;

@FunctionalInterface
public interface EventApplier<A, E> {

  A apply(A aggregate, E event);

}
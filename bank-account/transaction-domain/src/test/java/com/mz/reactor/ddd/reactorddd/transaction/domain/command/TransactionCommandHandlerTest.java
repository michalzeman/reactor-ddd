package com.mz.reactor.ddd.reactorddd.transaction.domain.command;

import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionAggregate;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionCreated;
import com.mz.reactor.ddd.reactorddd.transaction.domain.event.TransactionFailed;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCommandHandlerTest {

  TransactionCommandHandler subject = new TransactionCommandHandler();

  @Test
  void execute_CreateTransaction_OK() {
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();
    var command = CreateTransaction.builder()
        .aggregateId(aggregateId)
        .amount(BigDecimal.TEN)
        .correlationId(correlationId)
        .fromAccountId(fromAccountId)
        .toAccountId(toAccountId)
        .build();
    var aggregate = new TransactionAggregate(aggregateId);

    var result = subject.execute(aggregate, command);
    assertEquals(result.statusCode(), CommandResult.StatusCode.OK);
    assertTrue(result.events().stream().allMatch(e -> e instanceof TransactionCreated));
  }

  @Test
  void execute_CreateTransaction_FAILED() {
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var toAccountId = UUID.randomUUID().toString();
    var fromAccountId = UUID.randomUUID().toString();
    var command = CreateTransaction.builder()
        .aggregateId(aggregateId)
        .amount(BigDecimal.ZERO)
        .correlationId(correlationId)
        .fromAccountId(fromAccountId)
        .toAccountId(toAccountId)
        .build();
    var aggregate = new TransactionAggregate(aggregateId);

    var result = subject.execute(aggregate, command);
    assertEquals(result.statusCode(), CommandResult.StatusCode.FAILED);
    assertTrue(result.events().stream().allMatch(e -> e instanceof TransactionFailed));
  }
}
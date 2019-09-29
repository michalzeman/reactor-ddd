package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.mz.reactor.ddd.common.api.command.CommandResult;
import com.mz.reactor.ddd.reactorddd.account.domain.AccountAggregate;
import com.mz.reactor.ddd.reactorddd.account.domain.event.DepositMoneyFailed;
import com.mz.reactor.ddd.reactorddd.account.domain.event.MoneyDeposited;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountCommandHandlerTest {

  AccountCommandHandler accountCommandHandler = new AccountCommandHandler();

  @Test
  void executeDepositMoneyOK() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var accountAggregate = new AccountAggregate(aggregateId);
    var commandId = UUID.randomUUID().toString();
    var command = DepositMoney.builder()
        .commandId(commandId)
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.TEN)
        .build();

    //when
    var commandResult = accountCommandHandler.execute(accountAggregate, command);

    //then
    Assertions.assertEquals(commandResult.statusCode(), CommandResult.StatusCode.OK);
    Assertions.assertTrue(commandResult.events().stream().allMatch(e -> e instanceof MoneyDeposited));
  }

  @Test
  void executeDepositMoneyBadCommand() {
    //given
    var accountAggregate = new AccountAggregate(UUID.randomUUID().toString());

    //when
    var commandResult = accountCommandHandler.execute(accountAggregate, null);

    //then
    Assertions.assertEquals(commandResult.statusCode(), CommandResult.StatusCode.BAD_COMMAND);
  }

  @Test
  void executeDepositMoneyFailed() {
    //given
    var aggregateId = UUID.randomUUID().toString();
    var correlationId = UUID.randomUUID().toString();
    var accountAggregate = new AccountAggregate(aggregateId);
    var commandId = UUID.randomUUID().toString();
    var command = DepositMoney.builder()
        .commandId(commandId)
        .aggregateId(aggregateId)
        .correlationId(correlationId)
        .amount(BigDecimal.ZERO)
        .build();

    //when
    var commandResult = accountCommandHandler.execute(accountAggregate, command);

    //then
    Assertions.assertEquals(commandResult.statusCode(), CommandResult.StatusCode.FAILED);
    Assertions.assertTrue(commandResult.events().stream().allMatch(e -> e instanceof DepositMoneyFailed));
  }
}
package com.mz.reactor.ddd.reactorddd.account.domain.command;

import com.mz.reactor.ddd.common.api.command.Command;

public interface AccountCommand extends Command {

  String aggregateId();

}

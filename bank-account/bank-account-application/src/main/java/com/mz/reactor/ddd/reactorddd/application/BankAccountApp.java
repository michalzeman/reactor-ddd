package com.mz.reactor.ddd.reactorddd.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;


@SpringBootApplication
public class BankAccountApp {

  static {
    BlockHound.install();
  }

  public static void main(String[] args) {
//    Hooks.onOperatorDebug();
    SpringApplication.run(BankAccountApp.class, args);
  }

}

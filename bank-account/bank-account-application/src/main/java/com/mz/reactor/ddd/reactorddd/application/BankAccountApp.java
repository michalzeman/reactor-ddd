package com.mz.reactor.ddd.reactorddd.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;


@SpringBootApplication
public class BankAccountApp {

  public static void main(String[] args) {
    Hooks.onOperatorDebug();
    SpringApplication.run(BankAccountApp.class, args);
  }

}

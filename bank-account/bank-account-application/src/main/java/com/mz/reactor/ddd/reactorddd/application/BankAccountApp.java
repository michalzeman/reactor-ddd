package com.mz.reactor.ddd.reactorddd.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BankAccountApp {

//  @Autowired
//  private ObjectMapper objectMapper;

  static {
//    BlockHound.install();
  }

  public static void main(String[] args) {
//    Hooks.onOperatorDebug();
    SpringApplication.run(BankAccountApp.class, args);
  }

//  @PostConstruct
//  public void setUp() {
//    objectMapper.registerModule(new JavaTimeModule());
//  }

}

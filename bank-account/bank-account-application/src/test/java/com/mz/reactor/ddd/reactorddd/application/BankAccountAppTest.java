package com.mz.reactor.ddd.reactorddd.application;

import com.mz.reactor.ddd.reactorddd.account.domain.AccountState;
import com.mz.reactor.ddd.reactorddd.account.domain.command.CreateAccount;
import com.mz.reactor.ddd.reactorddd.account.http.model.CreateAccountRequest;
import com.mz.reactor.ddd.reactorddd.account.http.model.CreateAccountResponse;
import com.mz.reactor.ddd.reactorddd.transaction.api.model.CreateTransactionRequest;
import com.mz.reactor.ddd.reactorddd.transaction.api.model.CreateTransactionResponse;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionState;
import com.mz.reactor.ddd.reactorddd.transaction.domain.TransactionStatus;
import com.mz.reactor.ddd.reactorddd.transaction.domain.command.CreateTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountAppTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  public void testApp() throws InterruptedException {
    var correlationId = "testScenarioCreateAccountsAndTransferMoney";

    var accountId1 = "account_1";
    var accountId2 = "account_2";

    createAccount(accountId1, correlationId);
    createAccount(accountId2, correlationId);

    assertThat(getAllAccounts().size()).isEqualTo(2);

    var transactionId = createTransaction(accountId1, accountId2, correlationId, BigDecimal.TEN).payload().aggregateId();

    Thread.sleep(2000l);

    assertThat(getTransaction(transactionId).status()).isEqualByComparingTo(TransactionStatus.FINISHED);

    assertThat(getAccount(accountId1).amount().compareTo(BigDecimal.valueOf(90))).isEqualTo(0);
    assertThat(getAccount(accountId2).amount().compareTo(BigDecimal.valueOf(110))).isEqualTo(0);
  }

  private CreateAccountResponse createAccount(String accountId, String correlationId) {
    var createAccount1 = CreateAccountRequest.builder()
        .payload(CreateAccount.builder()
            .aggregateId(accountId)
            .balance(BigDecimal.valueOf(100))
            .correlationId(correlationId)
            .build())
        .build();
    return webTestClient.post().uri("/accounts")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(BodyInserters.fromObject(createAccount1))
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody(CreateAccountResponse.class)
        .returnResult().getResponseBody();
  }

  private List<AccountState> getAllAccounts() {
    return webTestClient.get().uri("/accounts")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody(List.class).returnResult().getResponseBody();
  }

  private CreateTransactionResponse createTransaction(String fromAccount, String toAccount, String correlationId, BigDecimal amount) {
    var createTransactionRequest = CreateTransactionRequest.builder()
        .payload(CreateTransaction.builder()
            .amount(amount)
            .correlationId(correlationId)
            .fromAccountId(fromAccount)
            .toAccountId(toAccount)
            .build())
        .build();

    return webTestClient.post().uri("/transactions")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .body(BodyInserters.fromObject(createTransactionRequest))
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody(CreateTransactionResponse.class).returnResult().getResponseBody();
  }

  private AccountState getAccount(String id) {
    return webTestClient.get().uri("/accounts/{id}", id)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
//        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody(AccountState.class).returnResult().getResponseBody();
  }

  private String getAccountString(String id) {
    return webTestClient.get().uri("/accounts/{id}", id)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus().isOk()
//        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody(String.class).returnResult().getResponseBody();
  }

  private TransactionState getTransaction(String id) {
    return webTestClient.get().uri("/transactions/{id}", id)
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
        .expectBody(TransactionState.class).returnResult().getResponseBody();
  }
}

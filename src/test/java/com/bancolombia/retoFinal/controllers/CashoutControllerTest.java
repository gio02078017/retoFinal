package com.bancolombia.retoFinal.controllers;

import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.Payment;
import com.bancolombia.retoFinal.repositories.CashoutRepository;
import com.bancolombia.retoFinal.repositories.UserRepository;
import com.bancolombia.retoFinal.services.interfaces.ICashoutService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CashoutControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ICashoutService cashoutService;

    Cashout cashout;

    @BeforeEach
    public void setup(){
        cashout = new Cashout();
        cashout.setId("0000000000000");
        cashout.setUserId("0000000000000111");
        cashout.setAmount(500.0);
    }

    @Test
    @Order(1)
    public void createCashout() {
        //when(cashoutRepository.findAllByUserId(cashout.getUserId())).thenReturn(Flux.just(cashout));
        webTestClient.post()
                .uri("/cashout/")
                .bodyValue(cashout)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cashout.class)
                .returnResult()
                .getResponseBody();

    }

    @Test
    @Order(2)
    public void getCashoutsByUserId() {
        when(cashoutService.getCashout(cashout.getUserId())).thenReturn(Flux.just(cashout));
        webTestClient.get()
                .uri("/cashout/user/{userId}", cashout.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(cashout.getId())
                .jsonPath("$[0].userId").isEqualTo(cashout.getUserId())
                .jsonPath("$[0].amount").isEqualTo(cashout.getAmount());

    }



}

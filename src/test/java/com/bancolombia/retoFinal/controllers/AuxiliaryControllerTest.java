package com.bancolombia.retoFinal.controllers;

import com.bancolombia.retoFinal.models.Amount;
import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.Payment;
import com.bancolombia.retoFinal.models.User;
import com.bancolombia.retoFinal.repositories.CashoutRepository;
import com.bancolombia.retoFinal.repositories.UserRepository;
import com.bancolombia.retoFinal.services.interfaces.IUserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuxiliaryControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CashoutRepository cashoutRepository;

    static Cashout cashout;

    @BeforeAll
    public static void setup(){
        cashout = new Cashout();
        cashout.setId("0000000000000");
        cashout.setUserId("0000000000000111");
        cashout.setAmount(500.0);

    }
    @AfterAll
    public static void tearDown(@Autowired UserRepository userRepository){
        userRepository.deleteAll().block();
    }

    @Test
    @Order(1)
    public void getCashoutsByUserId() {
        when(cashoutRepository.findAllByUserId(cashout.getUserId())).thenReturn(Flux.just(cashout));
        webTestClient.get()
                .uri("/auxiliary/transaction-history/user/{userId}", cashout.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(cashout.getId())
                .jsonPath("$[0].userId").isEqualTo(cashout.getUserId())
                .jsonPath("$[0].amount").isEqualTo(cashout.getAmount());

    }

    @Test
    @Order(2)
    public void testGetUserById() {
        //when(cashoutRepository.findAllByUserId(cashout.getUserId())).thenReturn(Flux.just(cashout));
        var payment = new Payment("00000", 500.0);
        webTestClient.post()
                .uri("/auxiliary/payments")
                .bodyValue(payment)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Payment.class)
                .returnResult()
                .getResponseBody();

    }

}

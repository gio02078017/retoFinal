package com.bancolombia.retoFinal.controllers;

import com.bancolombia.retoFinal.models.Amount;
import com.bancolombia.retoFinal.models.User;
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
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    IUserService userService;

    private static final AtomicLong userId = new AtomicLong();
    //private static  final Atom

    User newUser;

    @BeforeEach
    public void setup(){
        newUser = new User();
        newUser.setName("Reto Final");
        newUser.setBalance(500.0);
        newUser.setId("0000000000000");

    }
    @AfterAll
    public static void tearDown(@Autowired UserRepository userRepository){
        userRepository.deleteAll().block();
    }

    @Test
    @Order(1)
    public void testCreateUser(){

        var user = webTestClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();

        //userId.set(Objects.requireNonNull(user).getId());
    }

    @Test
    @Order(2)
    public void testGetAllUsers() {
        when(userService.getUsers()).thenReturn(Flux.just(newUser));
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
               .jsonPath("$[0].name").isEqualTo(newUser.getName())
               .jsonPath("$[0].balance").isEqualTo(newUser.getBalance());
    }

    @Test
    @Order(3)
    public void testGetUserById() {
        when(userService.getUserById(newUser.getId())).thenReturn(Mono.just(newUser));
        var result = webTestClient.get()
                .uri("users/{id}", newUser.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(newUser.getName())
                .jsonPath("$.balance").isEqualTo(newUser.getBalance());
    }

    @Test
    @Order(4)
    public void testUpdateUser() {

        var amount = new Amount();
        amount.setAmount(200);

        when(userService.getUserById(newUser.getId())).thenReturn(Mono.just(newUser));
        newUser.setBalance(newUser.getBalance() + amount.getAmount());
        when(userService.updateUser(newUser)).thenReturn(Mono.just(newUser));

        webTestClient.put()
                .uri("/users/{id}/balance", newUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(amount)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(newUser.getId())
                .jsonPath("$.name").isEqualTo(newUser.getName())
                .jsonPath("$.balance").isEqualTo(newUser.getBalance());
    }
}

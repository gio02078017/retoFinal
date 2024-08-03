package com.bancolombia.retoFinal.services;

import com.bancolombia.retoFinal.models.User;
import com.bancolombia.retoFinal.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    User user;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setName("Reto Final");
        user.setBalance(500.0);
        user.setId("0000000000000");
    }

    @Test
    void getUserById(){
        when(userRepository.findById(user.getId())).thenReturn(Mono.just(user));
        var resultado = userService.getUserById(user.getId());
        StepVerifier.create(resultado).expectNext(user).verifyComplete();
    }

    @Test
    void getUsers(){
        when(userRepository.findAll()).thenReturn(Flux.just(user));
        var resultado = userService.getUsers();
        StepVerifier.create(resultado).expectNext(user).verifyComplete();
    }

    @Test
    void updateUser(){
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        var resultado = userService.updateUser(user);
        StepVerifier.create(resultado).expectNext(user).verifyComplete();
    }

    @Test
    void createUser(){
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        var resultado = userService.createUser(user);
        StepVerifier.create(resultado).expectNext(user).verifyComplete();
    }

}

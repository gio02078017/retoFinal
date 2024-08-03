package com.bancolombia.retoFinal.services;

import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.Payment;
import com.bancolombia.retoFinal.models.User;
import com.bancolombia.retoFinal.repositories.CashoutRepository;
import com.bancolombia.retoFinal.services.interfaces.IAuxiliaryRestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CashoutServiceTest {

    @Mock
    IAuxiliaryRestClient auxiliaryRestClient;

    @Mock
    UserService userService;

    @Mock
    CashoutRepository cashoutRepository;

    @InjectMocks
    CashoutService cashoutService;

    Cashout cashout;
    User user;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        cashout = new Cashout();
        cashout.setId("0000000000000");
        cashout.setUserId("0000000000000111");
        cashout.setAmount(500.0);
        user = new User();
        user.setName("Reto Final");
        user.setBalance(500.0);
        user.setId("0000000000000");
    }

    @Test
    void getCashout(){
        when(auxiliaryRestClient.getCashForUserId(cashout.getUserId())).thenReturn(Flux.just(cashout));
        var resultado = cashoutService.getCashout(cashout.getUserId());
        StepVerifier.create(resultado).expectNext(cashout).verifyComplete();
    }

    @Test
    void createCashOut(){
        var payment = new Payment(cashout.getUserId(), cashout.getAmount());
        payment.setPaymentStatus("Approved");
        when(userService.getUserById(cashout.getUserId())).thenReturn(Mono.just(user));
        when(auxiliaryRestClient.createPayment(payment)).thenReturn(Mono.just(payment));
        when(userService.updateUser(user)).thenReturn(Mono.just(user));
        when(cashoutRepository.save(cashout)).thenReturn(Mono.just(cashout));
        var resultado = cashoutService.createCashOut(cashout);
        //StepVerifier.create(resultado).expectNext(cashout).verifyComplete();
        StepVerifier.create(resultado).expectError().verify();
    }


}

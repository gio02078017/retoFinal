package com.bancolombia.retoFinal.controllers;

import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.Payment;
import com.bancolombia.retoFinal.repositories.CashoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auxiliary")
public class AuxiliaryController {

    @Autowired
    private CashoutRepository cashoutRepository;

    @GetMapping("transaction-history/user/{userId}")
    public Flux<Cashout> getCashoutsByUserId(@PathVariable String userId) {
        return cashoutRepository.findAllByUserId(userId);
    }

    @PostMapping("/payments")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Payment> createCliente(@RequestBody Payment payment) {
        payment.setPaymentStatus(false);
        if(payment.getAmount() <= 200){
            payment.setPaymentStatus(true);
        }

        return Mono.just(payment);
    }

}

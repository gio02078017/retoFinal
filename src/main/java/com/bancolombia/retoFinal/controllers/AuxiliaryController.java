package com.bancolombia.retoFinal.controllers;

import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.Payment;
import com.bancolombia.retoFinal.repositories.CashoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

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
        var random = new Random();
        int randomIndex = random.nextInt(2);
        payment.setPaymentStatus(randomIndex == 0 ? "Approved" : "Rejected");
        //payment.setPaymentStatus("Rejected");
        System.out.println("resultado payment service "+ payment);
        return Mono.just(payment);
    }

}

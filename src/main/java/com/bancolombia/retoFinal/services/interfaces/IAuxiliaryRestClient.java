package com.bancolombia.retoFinal.services.interfaces;


import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.Payment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAuxiliaryRestClient {
    @PostExchange("/auxiliary/payments")
    Mono<Payment> createPayment(@RequestBody Payment payment);


    @GetExchange("/auxiliary/transaction-history/user/{userId}")
    Flux<Cashout> getCashForUserId(@PathVariable("userId") String userId);
}

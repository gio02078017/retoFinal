package com.bancolombia.retoFinal.services.interfaces;

import com.bancolombia.retoFinal.models.Cashout;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICashoutService {
   Mono<Cashout> createCashOut(Cashout cashout);
   Flux<Cashout> getCashout(String userId);
}

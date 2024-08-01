package com.bancolombia.retoFinal.repositories;

import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CashoutRepository extends ReactiveMongoRepository<Cashout, String> {
    Flux<Cashout> findAllByUserId(String UserId);
}

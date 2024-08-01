package com.bancolombia.retoFinal.services.interfaces;

import com.bancolombia.retoFinal.models.Amount;
import com.bancolombia.retoFinal.models.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import reactor.core.publisher.Mono;

public interface IUserRestClient {
    @GetExchange("/users/{userId}")
    Mono<User> getUserForId(@PathVariable("userId") String userId);

    @PostExchange("/users")
    Mono<User> createUser(@RequestBody User user);

    @PutExchange("/users/{userId}")
    Mono<User> updateUser(@PathVariable("userId") String userId, @RequestBody Amount amount);
}

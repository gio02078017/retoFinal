package com.bancolombia.retoFinal.services.interfaces;

import com.bancolombia.retoFinal.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<User> getUserById(String id);
    Flux<User> getUsers();
    Mono<User> updateUser(User user);
    Mono<User> createUser(User user);
}

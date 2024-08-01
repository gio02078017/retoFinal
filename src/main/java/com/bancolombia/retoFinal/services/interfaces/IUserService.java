package com.bancolombia.retoFinal.services.interfaces;

import com.bancolombia.retoFinal.models.User;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<User> getUserPorId(String id);
    Mono<User> updateUser(User user);
}

package com.bancolombia.retoFinal.services;

import com.bancolombia.retoFinal.config.ClientNotFoundException;
import com.bancolombia.retoFinal.config.Exception400Exception;
import com.bancolombia.retoFinal.models.User;
import com.bancolombia.retoFinal.repositories.UserRepository;
import com.bancolombia.retoFinal.services.interfaces.IUserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> getUserPorId(String id) {
        return userRepository.findById(id).switchIfEmpty(Mono.error(new ClientNotFoundException("El Usuario no existe")));
    }

    @Override
    public Mono<User> updateUser(User user) {
        return userRepository.save(user);
    }
}

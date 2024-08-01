package com.bancolombia.retoFinal.controllers;

import com.bancolombia.retoFinal.models.Amount;
import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.User;
import com.bancolombia.retoFinal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Flux<User> getAllClientes() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<User> getClienteById(@PathVariable String id) {
        return userRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createCliente(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}/balance")
    public Mono<User> updateCliente(@PathVariable String id, @RequestBody Amount amount) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setBalance(existingUser.getBalance() + amount.getAmount());
                    return userRepository.save(existingUser);
                });
    }
}

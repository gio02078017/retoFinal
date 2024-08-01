package com.bancolombia.retoFinal.services;

import com.bancolombia.retoFinal.config.ClientNotFoundException;
import com.bancolombia.retoFinal.config.Exception400Exception;
import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.models.Payment;
import com.bancolombia.retoFinal.repositories.CashoutRepository;
import com.bancolombia.retoFinal.services.interfaces.IAuxiliaryRestClient;
import com.bancolombia.retoFinal.services.interfaces.ICashoutService;
import com.bancolombia.retoFinal.services.interfaces.IUserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CashoutService implements ICashoutService {

    private final IAuxiliaryRestClient auxiliaryRestClient;
    private final IUserService userService;
    private final CashoutRepository cashoutRepository;

    public CashoutService(IAuxiliaryRestClient auxiliaryRestClient, IUserService userService, CashoutRepository cashoutRepository) {
        this.auxiliaryRestClient = auxiliaryRestClient;
        this.userService = userService;
        this.cashoutRepository = cashoutRepository;
    }

    @Override
    public Mono<Cashout> createCashOut(Cashout cashout) {
        return userService.getUserPorId(cashout.getUserId())
                .onErrorMap(throwable -> new ClientNotFoundException("Se debe crear el cliente anrtes de crear la poliza"))
                .doOnNext(user -> System.out.println("Nombre del cliente = "+user.getName()))
                .doOnError(throwable -> System.out.println("Se genero un problema "+throwable.getMessage()))
                .filter(user -> user.getBalance() >= cashout.getAmount())
                .flatMap(user ->
                    auxiliaryRestClient.createPayment(
                                    new Payment(cashout.getUserId(), cashout.getAmount())
                            )
                            .flatMap(payment -> {
                                       if(payment.getPaymentStatus()){
                                           return Mono.just(user);
                                       }
                                       return Mono.empty();
                                    })



                )
                .flatMap(existingUser -> {
                    existingUser.setBalance(existingUser.getBalance() - cashout.getAmount());
                    return userService.updateUser(existingUser);
                })
                .flatMap( user -> user != null ? cashoutRepository.save(cashout) : Mono.empty())
                .switchIfEmpty(Mono.error(new Exception400Exception("Creacion Fallida")));
    }

    @Override
    public Flux<Cashout> getCashout(String userId) {
        return auxiliaryRestClient.getCashForUserId(userId);
    }
}

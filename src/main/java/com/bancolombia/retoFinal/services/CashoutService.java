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
import reactor.util.function.Tuple2;

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
                //.onErrorMap(throwable -> new ClientNotFoundException("Se debe crear el cliente anrtes de crear la poliza"))
                //.onErrorMap(throwable)
                .doOnNext(user -> System.out.println("Nombre del cliente = "+user.getName()))
                .doOnError(throwable -> System.out.println("Se genero un problema "+throwable.getMessage()))
                .filter(user -> user.getBalance() >= cashout.getAmount())
                .switchIfEmpty(Mono.error(new Exception400Exception("saldo insuficiente")))
                .flatMap(user ->
                    auxiliaryRestClient.createPayment(
                                    new Payment(cashout.getUserId(), cashout.getAmount())
                            )//.zipWhen(Mono.just(user))
                            .zipWith(Mono.just(user))
                            /*.flatMap(payment -> {
                                       if(payment.getPaymentStatus()){
                                           return Mono.just(user);
                                       }
                                       return Mono.empty();
                                    })*/
                )
                .filter(tupla  -> tupla.getT1().getPaymentStatus().equals(Boolean.TRUE))
                .switchIfEmpty(Mono.error(new Exception400Exception("el pago no fue aprovado")))
                .flatMap(tupla -> {
                    tupla.getT2().setBalance(tupla.getT2().getBalance() - cashout.getAmount());
                    return userService.updateUser(tupla.getT2());
                })
                .flatMap( user -> user != null ? cashoutRepository.save(cashout) : Mono.empty())
                .switchIfEmpty(Mono.error(new Exception400Exception("Creacion Fallida")));
    }

    @Override
    public Flux<Cashout> getCashout(String userId) {
        return auxiliaryRestClient.getCashForUserId(userId);
    }
}

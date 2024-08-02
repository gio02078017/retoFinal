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
import reactor.util.retry.Retry;

import java.time.Duration;

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
                .doOnNext(user -> System.out.println("Nombre del cliente = "+user.getName()))
                .doOnError(throwable -> System.out.println("Se genero un problema "+throwable.getMessage()))
                .filter(user -> user.getBalance() >= cashout.getAmount())
                .switchIfEmpty(Mono.error(new Exception400Exception("saldo insuficiente")))
                .flatMap(user ->
                    auxiliaryRestClient.createPayment(
                                    new Payment(cashout.getUserId(), cashout.getAmount())
                            )
                            /*.doOnNext(payment -> System.out.println("Payment Status "+payment.getPaymentStatus().toString()))
                            .map(payment -> payment.getPaymentStatus().equals("Approved"))*/
                            .filter(payment -> payment.getPaymentStatus().equals("Approved"))
                            .switchIfEmpty(Mono.error(new Exception400Exception("El pago no fue aprovado")))
                            .doOnNext(mensaje -> System.out.println("mensaje "+mensaje))
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                                    .doBeforeRetry(mensaje -> System.out.println("mensaje Before Retry "+ mensaje))
                                    .filter(error -> !(error instanceof Exception400Exception))
                                    .doAfterRetry(mensaje -> System.out.println("mensaje After Retry "+ mensaje))
                            )
                            .doOnError(throwable -> System.out.println("Se genero un problema "+throwable.getMessage()))
                            .flatMap(resultado -> {
                                System.out.println("ultimo flagMap "+resultado);
                                return Mono.just(resultado);
                            })
                            .zipWith(Mono.just(user))
                )
                /*.filter(tupla  -> tupla.getT1().getPaymentStatus().equals("Approved"))
                .switchIfEmpty(Mono.error(new Exception400Exception("el pago no fue aprovado")))*/
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

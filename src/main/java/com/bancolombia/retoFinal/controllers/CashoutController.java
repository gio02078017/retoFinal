package com.bancolombia.retoFinal.controllers;


import com.bancolombia.retoFinal.models.Cashout;
import com.bancolombia.retoFinal.services.CashoutService;
import com.bancolombia.retoFinal.services.interfaces.ICashoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cashout")
public class CashoutController {


    //private final ICashoutService cashoutService;
    @Autowired
    private ICashoutService cashoutService;


    /*public CashoutController(ICashoutService cashoutService) {
        this.cashoutService = cashoutService;
    }*/

    @PostMapping("/")
    public Mono<Cashout> createCashout(@RequestBody Cashout cashout) {
        /*return userRestClient.getUserForId(cashout.getUserId())
                .flatMap(user ->
                        user.getBalance() >= cashout.getAmount()
                                ? cashoutRepository.save(cashout)
                                : Mono.empty()
                ).switchIfEmpty(Mono.error(new Exception400Exception("El cliente no tiene saldo")));*/
        /*return userRestClient.getUserForId(cashout.getUserId())
                .flatMap(user ->
                        user.getBalance() >= cashout.getAmount()
                                ? Mono.just(true)
                                : Mono.just(false)
                ).map(payment -> {
                    if(payment) {
                        var amount = new Amount();
                        amount.setAmount(cashout.getAmount());
                        return userRestClient.updateUser(cashout.getUserId(), amount);
                    }
                    return Mono.empty();

                }).flatMap(user -> user != null
                        ? cashoutRepository.save(cashout)
                        : Mono.empty())
                .switchIfEmpty(Mono.error(new Exception400Exception("El cliente no tiene saldo")));*/
        return cashoutService.createCashOut(cashout);
    }

    @GetMapping("user/{userId}")
    public Flux<Cashout> getCashoutByUserId(@PathVariable String userId) {
        //return cashoutRepository.findAll().filter(cashout -> cashout.getUserId().equals(userId));
        //return cashoutService.findAllByUserId(userId);
        return cashoutService.getCashout(userId);
    }
}

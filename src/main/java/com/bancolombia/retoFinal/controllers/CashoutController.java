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

    @Autowired
    private ICashoutService cashoutService;

    @PostMapping("/")
    public Mono<Cashout> createCashout(@RequestBody Cashout cashout) {
        return cashoutService.createCashOut(cashout);
    }

    @GetMapping("user/{userId}")
    public Flux<Cashout> getCashoutByUserId(@PathVariable String userId) {
        return cashoutService.getCashout(userId);
    }
}

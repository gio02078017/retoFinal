package com.bancolombia.retoFinal.config;

import com.bancolombia.retoFinal.services.interfaces.IAuxiliaryRestClient;
import com.bancolombia.retoFinal.services.interfaces.IUserRestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8085")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new ClientNotFoundException(errorBody)));
                })
                .build();
    }

    @Bean
    public IUserRestClient userRestClient(WebClient client) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(client)).build();
        return factory.createClient(IUserRestClient.class);
    }

    @Bean
    public IAuxiliaryRestClient auxiliaryRestClient(WebClient client) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(client)).build();
        return factory.createClient(IAuxiliaryRestClient.class);
    }
}

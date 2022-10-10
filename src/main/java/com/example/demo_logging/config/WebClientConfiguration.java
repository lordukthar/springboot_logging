package com.example.demo_logging.config;

import io.netty.handler.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
public class WebClientConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(WebClientConfiguration.class);
    private String proxyHost = "trend3.sbab.ad";
    private int proxyPort = 8080;
    @Value("${proxy.use}")
    boolean useProxy;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public ExchangeFilterFunction demoLambdaFilter() {
        ExchangeFilterFunction filterFunction = (clientRequest, nextFilter) -> {
            LOG.info("WebClient demoLambdaFilter executed");
            return nextFilter.exchange(clientRequest);
        };
        return filterFunction;
    }






    @Bean
    public WebClient jsonWebClient(ExchangeFilterFunction headerFilter) {

        HttpClient httpClient = null;

        System.out.println("FOO: " + activeProfile + " useProxy: " + useProxy);

        if (useProxy) {
            httpClient = HttpClient.create()
                    .wiretap("reactor.netty.http.client.HttpClient",
                            LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL)
                    .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP)
                            .host(proxyHost)
                            .port(proxyPort));;
        } else {
            httpClient = HttpClient.create()
                    .wiretap("reactor.netty.http.client.HttpClient",
                            LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);
        }

        return WebClient.builder()
               // .filter(WebClientFilters.demoFilter())
               /* .filter(new ExchangeFilterFunction() {
                    @Override
                    public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
                        return Mono.deferContextual(Mono::just)
                                .flatMap(context -> {


                                    System.out.println(context.toString());


                                    ClientRequest clientReq = ClientRequest.from(clientRequest)
                                            .header("APA_JJJ", "XXX")
                                            .build();

                                    return exchangeFunction.exchange(clientReq);
                                });
                    }
                })*/
            //see: https://github.com/jetty-project/jetty-reactive-httpclient
            //.clientConnector(new JettyClientHttpConnector())
            .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(headerFilter);
                })
                .defaultHeader("X-TEST-HEADER", "AJA")

            .codecs(
                    clientCodecConfigurer ->{
                        // use defaultCodecs() to apply DefaultCodecs
                        // clientCodecConfigurer.defaultCodecs();

                        // alter a registered encoder/decoder based on the default config.
                        // clientCodecConfigurer.defaultCodecs().jackson2Encoder(...)

                        // Or
                        // use customCodecs to register Codecs from scratch.
                        clientCodecConfigurer.customCodecs().register(new Jackson2JsonDecoder());
                        clientCodecConfigurer.customCodecs().register(new Jackson2JsonEncoder());
                    }

            )
            .exchangeStrategies(ExchangeStrategies.withDefaults())
            //                .exchangeFunction(ExchangeFunctions.create(new ReactorClientHttpConnector())
            //                        .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {})))
            //                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {clientRequest.}))
           //  .defaultHeaders(httpHeaders -> httpHeaders.
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .build();
    }

    @Bean
    public ExchangeFilterFunction headerFilter() {
        return (clientRequest, exchangeFunction) -> Mono.deferContextual(Mono::just)
                .flatMap(context -> {
                    System.out.println("FOO Filter 1: headerFilter");

                    ClientRequest newRequest = ClientRequest
                            .from(clientRequest)
                            .headers(httpHeaders -> {
                                if (context.hasKey("Authorization")) {
                                    httpHeaders.add("Authorization", context.get("Authorization"));
                                }

                                if (context.hasKey("requestId")) {
                                    httpHeaders.add("requestId", context.get("requestId"));
                                }

                                if (context.hasKey("User-Name")) {
                                    httpHeaders.add("User-Name", context.get("User-Name"));
                                }
                            })
                            .build();
                    return exchangeFunction.exchange(newRequest);
                }) ;

    }


}

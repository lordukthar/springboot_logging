package com.example.demo_logging.client;


import com.example.demo_logging.api.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class UserJsonClient {

    //https://jsonplaceholder.typicode.com/users
    Logger LOG = LoggerFactory.getLogger("OpenApiJsonClient");


    private final WebClient jsonWebClient;

    @Autowired
    public UserJsonClient(WebClient jsonWebClient) {
        this.jsonWebClient = jsonWebClient;
    }

    public Mono<User> getUser(Long id) {
        LOG.info("getUser {}", id);
        return jsonWebClient
                .get()
                .uri("/users/"+id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class)
                .log();
    }


    public Flux<User> getUsersToLog(String userName) {
        LOG.info("getUsers");
        return jsonWebClient

                //.def().header("User-Name-as-Param", userName)
                .get()
                .uri("/users")
                .header("User-Name-as-Param", userName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(User.class)
                .log()
                .timeout(Duration.ofMillis(500L));
    }
     /*
    List<Integer> list = Arrays.asList(1, 3, 5);
      Flux.fromIterable(list)
          .subscribe(System.out::println);
     */
    public Flux<User> getUsers() {
        LOG.info("getUsers");
        return jsonWebClient
                .get()
                .uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(User.class)
                .log()
                .timeout(Duration.ofMillis(500L));
    }

}

package com.example.demo_logging.service;

import com.example.demo_logging.api.User;
import com.example.demo_logging.client.UserJsonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    Logger LOG = LoggerFactory.getLogger("UserService");
    private final UserJsonClient userJsonClient;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final Map<Long, User> users = new HashMap<>();

    @Autowired
    public UserService(UserJsonClient userJsonClient) {
        this.userJsonClient = userJsonClient;
        users.put(1L, new User("Wim"));
        users.put(2L, new User("Simon"));
        users.put(3L, new User("Siva"));
        users.put(4L, new User("Josh"));
    }

    public Flux<User> findUsers() {
        LOG.info("findUsers");
        //return Flux.fromIterable(users.values());
        return userJsonClient.getUsers();
    }

    public Mono<ServerResponse> findUsersToLog(ServerRequest req){
        LOG.info("findUsersToLog {}", req.queryParam("apa").get());

        String nameAsParameter = req.queryParam("apa").orElse("No name");
        Flux<User> users = userJsonClient.getUsersToLog(nameAsParameter);
        return ServerResponse.ok().body(users, User.class);
    }

    public Mono<User> findUser(Long id) {
        LOG.error("findUser ({}) ", id);
        return Mono.just(users.get(id));
    }


}

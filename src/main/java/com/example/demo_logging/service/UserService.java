package com.example.demo_logging.service;

import com.example.demo_logging.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {


    private final Map<Long, User> users = new HashMap<>();

    public UserService() {
        users.put(1L, new User("Wim"));
        users.put(2L, new User("Simon"));
        users.put(3L, new User("Siva"));
        users.put(4L, new User("Josh"));
    }

    public Flux<User> findUsers() {
        return Flux.fromIterable(users.values());
    }


}

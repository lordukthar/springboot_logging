package com.example.demo_logging.routes;

import com.example.demo_logging.api.User;
import com.example.demo_logging.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class UserRoutes {

    Logger LOG = LoggerFactory.getLogger("UserRoutes");

    private final UserService userService;

    @Autowired
    public UserRoutes(UserService userService) {
        this.userService = userService;
    }

    @Bean
    RouterFunction<ServerResponse> getUsers() {
        LOG.trace("TRACE");
        LOG.error("ERROR");
        LOG.info("INFO");
        LOG.debug("DEBUG");

        return route(GET("/users"),
                req -> ok().body(
                        userService.findUsers(), User.class));
    }


    @Bean
    RouterFunction<ServerResponse> getUsersToLog() {
        LOG.trace("TRACE");
        LOG.error("ERROR");
        LOG.info("INFO");
        LOG.debug("DEBUG");

            return route(GET("/users-to-log").and(queryParam("apa", t -> true)),
                    userService::findUsersToLog);
        }


    @Bean
    RouterFunction<ServerResponse> getUser() {
        return route(GET("/users/{id}"),
                req -> ok().body(
                        userService.findUser(Long.valueOf(req.pathVariable("id"))), User.class));
    }

}

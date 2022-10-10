package com.example.demo_logging.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class LoggingFilter implements WebFilter {
    private static final Logger LOG = LoggerFactory.getLogger("WebConLoggingFiltertextFilter");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        LOG.info("LoggingFilter");

        UUID uniqueId = UUID.randomUUID();
        MDC.put("requestId", uniqueId.toString());
        LOG.info("Request IP address is {}", exchange.getRequest().getRemoteAddress());

        StringBuilder sb = new StringBuilder();

        if(exchange.getRequest().getHeaders().get("Authorization") != null &&
                exchange.getRequest().getHeaders().get("Authorization").size() > 0) {
            sb.append(exchange.getRequest().getHeaders().get("Authorization").get(0));
        } else {
            sb.append("Bearer ").append(UUID.randomUUID());
        }

        StringBuilder userName = new StringBuilder();

        if(exchange.getRequest().getHeaders().get("User-Name") != null &&
                exchange.getRequest().getHeaders().get("User-Name").size() > 0) {
            userName.append(exchange.getRequest().getHeaders().get("User-Name").get(0));
        } else {
            userName.append("User-Name not found");
        }

        MDC.put("Authorization", sb.toString());
        MDC.put("User-Name", userName.toString());

        //add header to response
        exchange.getResponse()
                .getHeaders()
                    .add("requestId",  uniqueId.toString());

        exchange.getResponse()
                .getHeaders()
                .add("Authorization",  sb.toString());

        LOG.info("Response header requestId is set with uuid {}", uniqueId);
        LOG.info("Response header Authorization is set with uuid {}", sb);
        LOG.info("Response header User name is set with name {}", userName);

        return chain.filter(exchange)
                .contextWrite(Context.of("requestId", uniqueId.toString()))
                .contextWrite(Context.of("User-Name", userName.toString()))
                .contextWrite(Context.of("Authorization", sb.toString()));
    }
}


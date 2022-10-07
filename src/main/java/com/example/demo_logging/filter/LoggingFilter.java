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

        //add header to response
        exchange.getResponse()
                .getHeaders().add("requestId",  uniqueId.toString());

        LOG.info("Response header is set with uuid {}", uniqueId.toString());

        return chain.filter(exchange)
                 .contextWrite(Context.of("requestId", uniqueId.toString()));

    }
}


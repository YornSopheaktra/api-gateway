package com.spt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spt.model.entity.Client;
import com.spt.service.AuthenticateManager;
import com.spt.utils.HttpUtilsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BaseGlobalFilter{

    @Value("${ignoreResources:**/actuator/health}")
    protected String ignoreResources;

    @Value("${token.endpoint:**/oauth/token/**}")
    protected String tokenEndpoint;

    private final AuthenticateManager authenticateManager;


    protected static final ObjectMapper mapper = new ObjectMapper();


    protected Client getClient(ServerWebExchange exchange) {
        return authenticateManager.
                instant(exchange.getRequest().getHeaders())
                .getClient();
    }

    boolean skipCheckUrl(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        List<String> ignoreResourcesList = Arrays.asList(ignoreResources.split(","));
        Optional<String> foundIgnoreResources = ignoreResourcesList.stream()
                .filter(resource -> HttpUtilsRequest.isIgnoreResources(resource, path)).
                        findFirst();
        return foundIgnoreResources.isPresent();
    }

    protected boolean isAuthTokenEndpoint(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        List<String> ignoreResourcesList = Arrays.asList(tokenEndpoint.split(","));
        Optional<String> foundIgnoreResources = ignoreResourcesList.stream()
                .filter(resource -> HttpUtilsRequest.isIgnoreResources(resource, path)).
                        findFirst();
        return foundIgnoreResources.isPresent();
    }


}

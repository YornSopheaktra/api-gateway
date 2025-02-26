package com.spt.filter;


import com.spt.model.constant.FilterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Component
@Slf4j
@RefreshScope
public class PreGlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {
    @Value("${ignoreResources:**/actuator/health}")
    private String ignoreResources;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("Global filter is filtering.");
        /*DefaultHttpLoggerFormatter.preLog(exchange);
        ServerHttpRequest request = exchange.getRequest();
        if (super.skipCheckUrl(exchange, chain))
            return chain.filter(exchange);
        Client client = super.getClient(exchange);
        this.validateClientDetails(client, exchange);
        this.filterClientScopes(exchange, client);
        exchange.getRequest()
                .mutate()
                .header("start_time", System.currentTimeMillis() + "")
                .header("channel_type", client.getChannelType())
                .header("channel",
                        ChannelType.valueOf(client.getChannelType()).getChannel().name());*/
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.GLOBAL_CLIENT_FILTER;
    }
/*
    private void filterClientScopes(ServerWebExchange exchange,
                                    Client client) {
        var scopes =
                client.buildScopes();
        String backendContext = APIUtilsPattern
                .getServiceAndDownStreamUrl(exchange.getRequest().getPath()
                        .value())._1;
        Optional<String> first = scopes
                .stream()
                .filter(sc -> sc.equals(backendContext))
                .findFirst();
        if (first.isEmpty())
            throw new AuthException(AuthCode.AUTH_INVALID_SCOPE);
    }

    private void validateClientDetails(Client client, ServerWebExchange exchange) {
        this.validateClientSecret(client, HttpHeaderUtils.clientSecret(exchange.getRequest().getHeaders()));
        //this.validateApiKey(client, HttpHeaderUtils.apiKey(exchange.getRequest().getHeaders()));
    }*/

}

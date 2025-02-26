package com.spt.filter;


import com.spt.model.constant.FilterOrder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;


@Component("oauth2_token")
@Slf4j
public class OAuthTokenFilter
        //extends BaseGlobalFilter
        implements GlobalFilter, Ordered {

//    @Autowired
//    private TokenUtils tokenUtils;
//
//    @Autowired
//    private RequestBodyRewrite requestBodyRewrite;
//
//    @Autowired
//    private ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter;
//
//    @Autowired
//    private ClientService clientService;
//
//    @Autowired
//    private TokenValidator tokenValidator;
//
//    @Value("${ignoreResources:**/actuator/health}")
//    private String ignoreResources;
//
//    OAuthTokenFilter(AuthenticateManager authenticateManager) {
//        super(authenticateManager);
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("OAuthTokenPolicy is filtering is started");
        /*if (super.skipCheckUrl(exchange, chain))
            return chain.filter(exchange);
        if (this.isAuthTokenEndpoint(exchange, chain)) {
            //validate client and secret if the endpoint is login/oauth-token/authorization
            var client = super.getClient(exchange);
            super.validateClientSecret(client, HttpHeaderUtils.clientSecret(exchange.getRequest().getHeaders()));
            return chain.filter(exchange);
        }
        Client client = clientService
                .findClientById(clientId(exchange.getRequest().getHeaders()));
        Optional<ClientPolicy> policyOAuth =
                client.isPolicyExisted(PolicyConstants.OAUTH2_TOKEN_POLICY);
        if (policyOAuth.isEmpty()) {
            return chain.filter(exchange);
        }
        Claims claims =
                tokenUtils.getClaim(tokenUtils.getAuthorizationToken(exchange.getRequest()),
                        exchange.getRequest().getHeaders().getFirst("channel_type"));
        tokenValidator
                .create(exchange, claims)
                .validate();
        exchange.getRequest()
                .mutate()
                .header(CIF, TokenUtils.ClaimUtils.getCif(claims))
                .build();

        MediaType contentType = exchange.getRequest()
                .getHeaders().getContentType();
        boolean compatibleWith = MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType);
        if (compatibleWith)
            return chain.filter(exchange);
        try {
            return modifyRequestBodyFilter.apply(
                    new ModifyRequestBodyGatewayFilterFactory.Config()
                            .setRewriteFunction(String.class,
                                    String.class, requestBodyRewrite))
                    .filter(exchange, chain);
        } catch (Exception exception) {
            log.warn("Exception occoured on body rewrite");
        }*/
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return FilterOrder.REWRITE_TOKEN_ORDER;
    }

}
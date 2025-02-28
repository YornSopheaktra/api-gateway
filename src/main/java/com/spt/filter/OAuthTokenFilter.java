package com.spt.filter;


import com.spt.model.constant.FilterOrder;
import com.spt.model.entity.Client;
import com.spt.service.AuthenticateManager;
import com.spt.service.RequestBodyRewrite;
import com.spt.service.TokenValidator;
import com.spt.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.spt.utils.TokenUtils.USER_ID;


@Component("oauth2_token")
@Slf4j
public class OAuthTokenFilter
        extends BaseGlobalFilter
        implements GlobalFilter, Ordered {


    private final TokenUtils tokenUtils;


    private final RequestBodyRewrite requestBodyRewrite;

    private final ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter;


    private final TokenValidator tokenValidator;

    @Value("${ignoreResources:**/actuator/health}")
    private String ignoreResources;

    public OAuthTokenFilter(AuthenticateManager authenticateManager, TokenUtils tokenUtils, RequestBodyRewrite requestBodyRewrite, ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter, TokenValidator tokenValidator) {
        super(authenticateManager);
        this.tokenUtils = tokenUtils;
        this.requestBodyRewrite = requestBodyRewrite;
        this.modifyRequestBodyFilter = modifyRequestBodyFilter;
        this.tokenValidator = tokenValidator;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("OAuthTokenPolicy is filtering is started");
        if (super.skipCheckUrl(exchange, chain))
            return chain.filter(exchange);
        if (this.isAuthTokenEndpoint(exchange, chain)) {
            //validate client and secret if the endpoint is login/oauth-token/authorization
            var client = super.getClient(exchange);
            super.validateClientDetails(client, exchange);
            return chain.filter(exchange);
        }
        Client client = super.getClient(exchange);
/*        Optional<ClientPolicy> policyOAuth =
                client.isPolicyExisted(PolicyConstants.OAUTH2_TOKEN_POLICY);
        if (policyOAuth.isEmpty()) {
            return chain.filter(exchange);
        }*/
        Claims claims =
                tokenUtils.getClaim(tokenUtils.getAuthorizationToken(exchange.getRequest()),
                        exchange.getRequest().getHeaders().getFirst("channel_type"));
        tokenValidator
                .create(exchange, claims)
                .validate();
        exchange.getRequest()
                .mutate()
                .header(USER_ID, TokenUtils.ClaimUtils.getCif(claims))
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
        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return FilterOrder.REWRITE_TOKEN_ORDER;
    }

}
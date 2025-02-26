package com.spt.filter;


import com.spt.model.constant.FilterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

//import static com.hatthabank.gateway.constants.APIRequestHeaderConstants.INTERNAL_API_CODE;

@Component("authorize_code")
@Slf4j
public class AuthorizationRoleFilter
        //extends BaseGlobalFilter
        implements org.springframework.cloud.gateway.filter.GlobalFilter,
        Ordered {

    /*@Autowired
    private APIUtilsPattern apiUtilsPattern;

    @Autowired
    private APIService apiService;

    @Autowired
    private RoleAPIService roleAPIService;

    @Autowired
    private TokenUtils tokenUtils;*/



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("AuthorizationRoleFilter is filtering");
        Long start = System.currentTimeMillis();
        log.debug("AuthorizationRoleFilter with start:{}", start);
        /*if (super.skipCheckUrl(exchange, chain))
            return chain.filter(exchange);
        Client client = super.getClient(exchange);
        String roleCode;
        // Check Policy
        Optional<ClientPolicy> clientPolicy = client
                .isPolicyExisted(PolicyConstants.AUTHORIZATION_POLICY);
        if (clientPolicy.isEmpty())
            return chain.filter(exchange);
        BasicAuthPolicyConfig config =
                parseConfiguration(clientPolicy.get().getPolicyConfig());
        Optional<ClientRole> first = client.getClientRoles()
                .stream()
                .filter(cr -> cr.getRoleType().equals(config.getRoleType()))
                .findFirst();
        ClientRole clientRole = first
                .orElseThrow(() -> new AuthException(AuthCode.AUTH_INVALID_CLIENT_ROLE));
        if (config.getRoleType().equals(RoleType.CLIENT_DIRECT)) {
            roleCode = clientRole.getRole().getCode();
        } else if (RoleType.JWT_TOKEN
                .equals(config.getRoleType())) {
            roleCode = tokenUtils.getClaim(tokenUtils.getAuthorizationToken(exchange.getRequest()))
                    .get(TokenUtils.ROLES).toString();
        } else throw new AuthException(AuthCode.AUTH_INVALID_ROLE);
        this.authorizeAPIByRole(roleCode, exchange.getRequest());*/
        log.trace("AuthorizationRoleFilter with end:{}", (System.currentTimeMillis() - start));
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return FilterOrder.SECURITY_HEADER_ORDER;
    }
/*
    public void authorizeAPIByRole(String roleCode, ServerHttpRequest request) {
        String apiCde = request.getHeaders().getFirst(INTERNAL_API_CODE);
        if (!StringUtils.hasLength(apiCde)) {
            APIDef matchingApi = apiUtilsPattern.getMatchingApi(request.getPath().value(), request.getMethod(), apiService.findAll());
            apiCde = matchingApi.getCode();
        }
        this.roleAPIService.findByRoleCodeAndApiCode(roleCode, apiCde);
    }

    public BasicAuthPolicyConfig parseConfiguration(String jsonConfiguration)
            throws ConfigurationParseException {
        try {
            return mapper.readerFor(BasicAuthPolicyConfig.class).readValue(jsonConfiguration);
        } catch (Exception e) {
            throw new ConfigurationParseException(e);
        }
    }*/

}

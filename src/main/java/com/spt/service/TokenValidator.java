package com.spt.service;


import com.spt.exption.AuthException;
import com.spt.exption.code.AuthCode;
import com.spt.utils.HttpHeaderUtils;
import com.spt.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import static com.spt.model.constant.APIRequestHeaderConstants.DEVICE_ID;
import static com.spt.utils.TokenUtils.ROLES;


@Getter
@Component
public class TokenValidator {

    private Claims claims;
    private ServerWebExchange exchange;
    public TokenValidator create(ServerWebExchange exchange,
                                 Claims claims){
        this.claims=claims;
        this.exchange=exchange;
        return this;
    }

    public void validate(){
        this.accessTokenMustHaveRole();
        this.deviceMatching();
    }

    private void accessTokenMustHaveRole() {
        if (!claims.containsKey(ROLES)) {
            throw new AuthException(AuthCode.AUTH_NO_PERMISSION);
        }
    }

    private void deviceMatching() {
        String channelType = TokenUtils.ClaimUtils.getChannelType(this.getClaims());
        if (channelType.equals("customer_app")) {
            String deviceId = String.valueOf(claims.get(DEVICE_ID));
            String requestDevice = HttpHeaderUtils
                    .deviceId(exchange.getRequest().getHeaders());
            if (!StringUtils.hasLength(requestDevice) || !requestDevice.equals(deviceId)) {
                throw new AuthException(AuthCode.AUTH_INVALID_TOKEN_DEVICE);
            }
        }
    }
}

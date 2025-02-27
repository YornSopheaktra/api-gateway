package com.spt.exption;

import com.spt.exption.code.AuthCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends GatewayException {

    private AuthCode authCode;

    public AuthException() {
    }


    public AuthException(String code, String message) {
        super(code, message);
        this.setStatus(HttpStatus.UNAUTHORIZED);
    }

    public AuthException(AuthCode authCode) {
        super(authCode);
        this.setStatus(HttpStatus.UNAUTHORIZED);
    }

    public AuthException(AuthCode authCode, HttpStatus status) {
        super(authCode);
        this.setStatus(status);
    }

}

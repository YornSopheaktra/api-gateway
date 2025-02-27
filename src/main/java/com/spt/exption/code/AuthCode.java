package com.spt.exption.code;

public enum AuthCode implements ErrorMessageCode {

    AUTH_INVALID_HEADER("M0001", "Http request header is invalid."),
    AUTH_INVALID_CLIENT_CREDENTIALS("M0002", "Client details is invalid."),
    AUTH_NO_PERMISSION("M0003", "Access denied."),
    AUTH_APIKEY_INVALID("M0004", "API key is invalid."),
    AUTH_GRANT_TYPE_INVALID("M0005", "grant_type is invalid."),
    AUTH_INVALID_TOKEN("M0006", "Access token is invalid."),
    AUTH_INVALID_TOKEN_DEVICE("M0011", "Access token or device is invalid."),
    AUTH_INVALID_CLIENT("M0007", "Client is not found"),
    AUTH_INVALID_USER_CREDENTIALS("M0008", " username or password is invalid."),
    AUTH_INVALID_REFRESH_TOKEN("M0009", "Refresh token is invalid."),
    AUTH_INVALID_IP("M0010", "IP is not allowed."),
    AUTH_INVALID_SESSION("E9000", "Session is invalid."),
    AUTH_INVALID_SESSION_EXPIRED("E9013", "Session is expired."),
    AUTH_INVALID_ROLE("E0009", "Access role is invalid"),
    AUTH_INVALID_CLIENT_ROLE("E0010", "Client role is invalid."),
    AUTH_INVALID_PIN("E0011", "Unauthorized the payment."),
    AUTH_INVALID_DEVICE("E0012", "Device id is required."),
    AUTH_INVALID_REQUEST("E0013", "Bad http request.The request is expired."),
    AUTH_INVALID_SCOPE("M00014", "Request service is unauthorized.");

    String code;
    String message;



    AuthCode(String code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.prefix() + code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

package com.spt.exption;


import com.spt.exption.code.ErrorMessageCode;
import org.springframework.http.HttpStatus;

public class ServerException extends GatewayException{

    ErrorMessageCode serverMessageCode;
    HttpStatus status;

    public ServerException() {
    }

    public ServerException(String code, String message) {
        super(code, message);
    }

    public ServerException(String code, String message, HttpStatus status) {
        super(code, message,status);
    }

    public ServerException(ErrorMessageCode errorMessageCode) {
        super(errorMessageCode);
    }
    public ServerException(ErrorMessageCode errorMessageCode, HttpStatus httpStatus) {
        super(errorMessageCode,httpStatus);
    }


}
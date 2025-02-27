package com.spt.utils;


import com.spt.exption.AuthException;
import com.spt.exption.code.AuthCode;
import com.spt.model.constant.APIRequestHeaderConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class HttpHeaderUtils {

    public static String clientId(HttpHeaders headers) {
        if (headers.containsKey(APIRequestHeaderConstants.CLIENT_ID)) {
            return headers.get(APIRequestHeaderConstants.CLIENT_ID).get(0);
        } else {
            return "";
        }
    }

    public static String deviceId(HttpHeaders headers) {
        if (headers.containsKey(APIRequestHeaderConstants.DEVICE_ID)) {
            return headers.get(APIRequestHeaderConstants.DEVICE_ID).get(0);
        } else {
            throw new AuthException(AuthCode.AUTH_INVALID_HEADER, HttpStatus.BAD_REQUEST);
        }
    }


    public static String apiKey(HttpHeaders headers) {
        if (headers.containsKey(APIRequestHeaderConstants.API_KEY))
            return headers.get(APIRequestHeaderConstants.API_KEY)
                    .get(0);
        else return "";
    }

    public static String clientSecret(HttpHeaders headers) {
        if (headers.containsKey(APIRequestHeaderConstants.CLIENT_SECRET)) {
            return headers.get(APIRequestHeaderConstants.CLIENT_SECRET).get(0);
        } else {
            return "";
        }
    }

    public static Long getClientRequestTimestamp(HttpHeaders headers) {
        if (headers.containsKey(APIRequestHeaderConstants.CLIENT_TIMESTAMP)) {
            return Long.parseLong(headers.get(APIRequestHeaderConstants.CLIENT_TIMESTAMP)
                    .get(0));
        } else {
            throw new AuthException(AuthCode.AUTH_INVALID_HEADER, HttpStatus.BAD_REQUEST);
        }
    }

    public static String getDigest(HttpHeaders headers) {
        if (headers.containsKey(APIRequestHeaderConstants.DIGEST)) {
            var rawDigest = headers.get(APIRequestHeaderConstants.DIGEST)
                    .get(0);
            StringBuilder digestBuilder =
                    new StringBuilder().append(APIRequestHeaderConstants.DIGEST_ALGO).append("=");
            return rawDigest.replace(digestBuilder.toString(), "");
        } else {
            throw new AuthException(AuthCode.AUTH_INVALID_HEADER, HttpStatus.BAD_REQUEST);
        }
    }

    public static String getOs(HttpHeaders headers) {
        String s = deviceId(headers);
        if (StringUtils.hasLength(s)) {
            if (s.toLowerCase().startsWith("ios"))
                return "ios";
            else return "android";
        }
        return "";

    }
}

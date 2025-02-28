package com.spt.utils;

import com.spt.exption.AuthException;
import com.spt.exption.code.AuthCode;
import com.spt.model.Token;
import io.jsonwebtoken.*;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TokenUtils {

    public static final String ROLES = "AUTH_ROLES";
    public static final String CHANNEL = "channel_type";
    public static final String AUTH_HEADER = "Authorization";
    public static final String X_TOKEN = "x-token";
    public static final String CIF = "cif";
    public static final String USER_ID = "user_id";

    @Value("${rsa.private-key:}")
    private String internalRSAPrivateKey;

    @Value("${rsa.public-key:}")
    private String internalRSAPublicKey;

    @Value("${rsa.user-auth.public-key:}")
    private String userAuthPublicKey;

    public Claims getClaim(String accessToken, String channelType) {
        String publicKey;
        if (channelType.equalsIgnoreCase("agent_app")
                || channelType.equalsIgnoreCase("customer_app")) {
            publicKey = userAuthPublicKey;
        } else publicKey = internalRSAPublicKey;
        return this.isValidToken(accessToken, publicKey);
    }

    public Claims getClaim(String accessToken) {
        return this.isValidToken(accessToken, internalRSAPublicKey);
    }

    private Tuple3<Boolean, String, Claims> verifyToken(String accessToken, String publicKey) {
        return this.verifyTokenInternal(accessToken, publicKey);
    }

    private Tuple3<Boolean, String, Claims> verifyTokenInternal(String accessToken, String publicKey) {
        String msg = AuthCode.AUTH_INVALID_TOKEN.getMessage();
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SecurityUtils.readPublicKey(publicKey))
                    .parseClaimsJws(accessToken);
            Claims body = claimsJws.getBody();
            return Tuple.of(Boolean.TRUE, "Valid token.", body);
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            msg = "Invalid access token.";
            log.error(msg);
        } catch (ExpiredJwtException ex) {
            msg = "Access token is expired.";
            log.error(msg);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return Tuple.of(Boolean.FALSE, msg, null);
    }

    public Claims isValidToken(String accessToken, String publicKey) {
        Tuple3<Boolean, String, Claims> isValid =
                this.verifyToken(accessToken, publicKey);
        if (!isValid._1) {
            String msg = isValid._2;
            throw new AuthException(AuthCode.AUTH_INVALID_TOKEN.getCode(), msg);
        }
        return isValid._3();
    }

    public String getAuthorizationToken(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey(AUTH_HEADER)) {
            throw new AuthException(AuthCode.AUTH_INVALID_HEADER);
        }
        return request.getHeaders()
                .getFirst(AUTH_HEADER)
                .replace(Token.TOKEN_TYPE, "").trim();
    }
/*
    private String genRefreshToken(TokenGenerateRequest tokenCreate) {
        return this.createToken(tokenCreate.getId(), tokenCreate.getRefreshTokenExpired(),
                tokenCreate.getClientId(), null, null,
                tokenCreate.getChannelType(), tokenCreate.getDeviceId());
    }

    public Token generateToken(TokenGenerateRequest tokenCreate) {
        String accessToken = this.createToken(tokenCreate.getId(), tokenCreate.getAccessTokenExpired(),
                tokenCreate.getClientId(), tokenCreate.getRoles(), tokenCreate.getUserId(),
                tokenCreate.getChannelType(), tokenCreate.getDeviceId());
        Token token = new Token();
        token.setType(Token.TOKEN_TYPE);
        token.setAccessToken(accessToken);
        token.setRefreshToken(this.genRefreshToken(tokenCreate));
        return token;
    }

    public Token generateOnlyAccessToken(TokenGenerateRequest tokenCreate) {
        String accessToken = this.createToken(tokenCreate.getId(), tokenCreate.getAccessTokenExpired(),
                tokenCreate.getClientId(), tokenCreate.getRoles(), tokenCreate.getUserId(),
                tokenCreate.getChannelType(), tokenCreate.getDeviceId());
        Token token = new Token();
        token.setType(Token.TOKEN_TYPE);
        token.setAccessToken(accessToken);
        return token;
    }

    private String createToken(
            String id,
            Long tokenExpired, String clientId,
            String roles, String userId, ChannelType channelType, String deviceId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpired);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuer(clientId)
                .setId(id)
                .claim(ROLES, roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim(CHANNEL, channelType)
                .claim(CIF, userId)
                .claim(APIRequestHeaderConstants.DEVICE_ID, deviceId)
                .signWith(SignatureAlgorithm.RS256, SecurityUtils.readPrivateKey(internalRSAPrivateKey));
        return jwtBuilder.compact();
    }



    public String getXTokenSign(ServerHttpRequest request) {
        if (!request.getHeaders().containsKey(X_TOKEN)) {
            throw new AuthException(AuthCode.AUTH_INVALID_HEADER);
        }
        return request.getHeaders()
                .get(X_TOKEN)
                .get(0).replace(Token.TOKEN_TYPE, "")
                .trim();
    }



    */

    public static class ClaimUtils {

        public static String getCif(Claims claims) {
            return claims.getOrDefault(CIF, "").toString();
        }

        public static String getChannelType(Claims claims) {
            return claims.getOrDefault(CHANNEL, "INVALID").toString();
        }

        public static String getChannel(Claims claims) {
            return getChannelType(claims);
        }

    }
}

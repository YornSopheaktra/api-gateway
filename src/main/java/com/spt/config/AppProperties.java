package com.spt.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Getter
@ConfigurationProperties(prefix = "gw")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppProperties {

    private Auth auth = new Auth();
    private OAuth2 oauth2 = new OAuth2();
    private Security security = new Security();

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Security{
        private String salt;
        private String secretCode;
        private String signKey;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
        private long refreshTokenExpirationMsec;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }

}
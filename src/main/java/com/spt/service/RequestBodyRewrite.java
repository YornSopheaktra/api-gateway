package com.spt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spt.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class RequestBodyRewrite implements RewriteFunction<String, String> {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    private final TokenUtils tokenUtils;

    @Value("${ignore.remove-cif:}")
    String lisPathToIgnore;

    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String body) {
        Map<String, Object> bodyMap = new HashMap<>();
        try {
            if (body != null)
                bodyMap = objectMapper.readValue(body, Map.class);
            Claims claims = tokenUtils.getClaim(tokenUtils.getAuthorizationToken(exchange.getRequest()),
                    exchange.getRequest().getHeaders().getFirst("channel_type"));
            //ChannelType channelType = getChannelType(claims);
            //bodyMap.put("channel_type", channelType.name());
            //bodyMap.put("channel", channelType.getChannel().name());
            this.extractCIFFromJWTToken(bodyMap, exchange, "customer_app", claims);
            return Mono.just(objectMapper.writeValueAsString(bodyMap));
        } catch (Exception ex) {
            log.error(
                    "Rewrite the access token in body has issue:{}", ex);
        }
        return Mono.just(body);
    }

    private void extractCIFFromJWTToken(Map<String, Object> map,
                                        ServerWebExchange exchange, String channel,
                                        Claims claims) {
        if (this.removeCifFromBody(exchange, lisPathToIgnore, channel, map))
            map.put("user_id", TokenUtils.ClaimUtils
                    .getCif(claims));
    }

    private boolean removeCifFromBody(ServerWebExchange exchange,
                                      String pathStrConfig,
                                      String channel, Map<String, Object> body) {
        if (isMobileChannelType(channel)) {
//            body.put("os", HttpHeaderUtils.getOs(exchange.getRequest().getHeaders()));
            return checkPathIfRemove(exchange, pathStrConfig);
        } else
            return false;
    }

    private boolean isMobileChannelType(String channel) {
        return channel.equals("customer_app");
    }

    private boolean checkPathIfRemove(ServerWebExchange exchange, String pathStrConfig) {
        List<String> pathAllowToSendCif =
                Arrays.asList(pathStrConfig.split("\\s*,\\s*"));
        for (String cPath :
                pathAllowToSendCif) {
            PathPattern pathInDB = PathPatternParser.defaultInstance.parse(cPath);
            PathContainer pathContainer = PathContainer
                    .parsePath(exchange.getRequest().getPath().value());
            if (pathInDB.matches(pathContainer)) {
                return false;
            }
        }
        return true;
    }

}

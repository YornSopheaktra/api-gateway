package com.spt.config;

import com.spt.model.entity.Client;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@ConfigurationProperties(prefix = "client")
@Configuration
@Getter
@Setter
public class ClientConfig {
    Map<String, Client> config;
}

package com.appswave.membership.util;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter
public class JwtConfigProperties {
    private Long expiration;
    private String header;
    private String prefix;
    private String secret;
}

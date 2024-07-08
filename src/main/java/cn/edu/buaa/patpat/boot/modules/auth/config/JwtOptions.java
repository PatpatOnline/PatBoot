package cn.edu.buaa.patpat.boot.modules.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtOptions {
    private String secret;
    private int jwtExpiration;
    private int refreshExpiration;
}

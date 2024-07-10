package cn.edu.buaa.patpat.boot.modules.auth.config;

import cn.edu.buaa.patpat.boot.config.CookiesOptions;
import cn.edu.buaa.patpat.boot.extensions.cookies.CookieSetter;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.extensions.jwt.IJwtIssuer;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtIssuer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthConfig {
    public static final String AUTH_ATTR = "auth";

    private final JwtOptions jwtOptions;
    private final CookiesOptions cookiesOptions;

    @Bean(name = "jwtIssuer")
    public IJwtIssuer getJwtIssuer() {
        return new JwtIssuer(jwtOptions.getJwtExpiration() * 1000, jwtOptions.getSecret());
    }

    @Bean(name = "refreshIssuer")
    public IJwtIssuer getRefreshIssuer() {
        return new JwtIssuer(jwtOptions.getRefreshExpiration() * 1000, jwtOptions.getSecret());
    }

    @Bean(name = "jwtCookieSetter")
    public ICookieSetter getJwtCookieSetter() {
        return new CookieSetter(
                CookiesOptions.JWT_COOKIE,
                cookiesOptions.getDomain(),
                CookiesOptions.PATH,
                jwtOptions.getJwtExpiration(),
                cookiesOptions.isHttpOnly(),
                cookiesOptions.isSecure());
    }

    @Bean(name = "refreshCookieSetter")
    public ICookieSetter getRefreshCookieSetter() {
        return new CookieSetter(
                CookiesOptions.JWT_REFRESH_COOKIE,
                cookiesOptions.getDomain(),
                CookiesOptions.PATH,
                jwtOptions.getRefreshExpiration(),
                cookiesOptions.isHttpOnly(),
                cookiesOptions.isSecure());
    }
}

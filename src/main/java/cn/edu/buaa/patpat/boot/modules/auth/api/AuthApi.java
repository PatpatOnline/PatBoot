/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.auth.api;

import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtIssueException;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtVerifyException;
import cn.edu.buaa.patpat.boot.modules.auth.config.AuthConfig;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.auth.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthApi {
    private final AuthService authService;
    private final ICookieSetter jwtCookieSetter;

    public String issueJwt(AuthPayload auth) throws JwtIssueException {
        return authService.issueJwt(auth);
    }

    public AuthPayload verifyJwt(String jwt) throws JwtVerifyException {
        return authService.verifyJwt(jwt);
    }

    public String issueRefresh(AuthPayload auth) throws JwtIssueException {
        return authService.issueRefresh(auth);
    }

    public Cookie setJwtCookie(String jwt) {
        return authService.setJwtCookie(jwt);
    }

    public Cookie cleanJwtCookie() {
        return authService.cleanJwtCookie();
    }

    public Cookie setRefreshCookie(String refresh) {
        return authService.setRefreshCookie(refresh);
    }

    public Cookie cleanRefreshCookie() {
        return authService.cleanRefreshCookie();
    }

    public String getJwt(HttpServletRequest request) {
        return jwtCookieSetter.get(request);
    }

    public AuthPayload getAuth(HttpServletRequest request) {
        return (AuthPayload) request.getAttribute(AuthConfig.AUTH_ATTR);
    }
}

/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.auth.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.extensions.jwt.IJwtIssuer;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtIssueException;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtVerifyException;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService extends BaseService {
    private final IJwtIssuer jwtIssuer;
    private final IJwtIssuer refreshIssuer;
    private final ICookieSetter jwtCookieSetter;
    private final ICookieSetter refreshCookieSetter;

    public String issueJwt(AuthPayload auth) throws JwtIssueException {
        String subject;
        try {
            subject = mappers.toJson(auth);
        } catch (JsonProcessingException e) {
            throw new JwtIssueException("Failed to serialize auth view", e);
        }

        return jwtIssuer.issue(subject);
    }

    public AuthPayload verifyJwt(String jwt) throws JwtVerifyException {
        return validateJwt(jwtIssuer, jwt);
    }

    public String issueRefresh(AuthPayload auth) throws JwtIssueException {
        String subject;
        try {
            subject = mappers.toJson(auth);
        } catch (JsonProcessingException e) {
            throw new JwtIssueException("Failed to serialize auth view", e);
        }
        return refreshIssuer.issue(subject);
    }

    public AuthPayload verifyRefresh(String refresh) throws JwtVerifyException {
        return validateJwt(refreshIssuer, refresh);
    }

    public Cookie setJwtCookie(String jwt) {
        return jwtCookieSetter.set(jwt);
    }

    public Cookie cleanJwtCookie() {
        return jwtCookieSetter.clean();
    }

    public Cookie setRefreshCookie(String refresh) {
        return refreshCookieSetter.set(refresh);
    }

    public Cookie cleanRefreshCookie() {
        return refreshCookieSetter.clean();
    }

    private AuthPayload validateJwt(IJwtIssuer issuer, String jwt) throws JwtVerifyException {
        if (jwt == null) {
            throw new JwtVerifyException("JWT is null");
        }

        String subject = issuer.verify(jwt);
        try {
            return mappers.fromJson(subject, AuthPayload.class);
        } catch (JsonProcessingException e) {
            throw new JwtVerifyException("Failed to deserialize auth view", e);
        }
    }
}

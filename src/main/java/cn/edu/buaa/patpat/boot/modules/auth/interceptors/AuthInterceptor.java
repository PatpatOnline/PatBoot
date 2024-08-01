package cn.edu.buaa.patpat.boot.modules.auth.interceptors;

import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.exceptions.UnauthorizedException;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtIssueException;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtVerifyException;
import cn.edu.buaa.patpat.boot.modules.auth.config.AuthConfig;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.auth.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final ICookieSetter jwtCookieSetter;
    private final ICookieSetter refreshCookieSetter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String jwt = jwtCookieSetter.get(request);
        if (jwt == null) {
            throw new UnauthorizedException(M("auth.permission.jwt.missing"));
        }

        AuthPayload payload;

        // Validate JWT first.
        try {
            payload = authService.verifyJwt(jwt);
            request.setAttribute(AuthConfig.AUTH_ATTR, payload);
            return true;
        } catch (JwtVerifyException e) {
            // JWT expired, try refresh token.
        }

        // Validate refresh token second.
        try {
            payload = authService.verifyRefresh(jwtCookieSetter.get(request));
            String newJwt = authService.issueJwt(payload);
            String newRefresh = authService.issueRefresh(payload);
            Cookie jwtCookie = jwtCookieSetter.set(newJwt);
            Cookie refreshCookie = refreshCookieSetter.set(newRefresh);
            response.addCookie(jwtCookie);
            response.addCookie(refreshCookie);
        } catch (JwtVerifyException e) {
            throw new UnauthorizedException(M("auth.permission.jwt.expired"));
        } catch (JwtIssueException e) {
            log.error("Failed to issue new JWT: {}", e.getMessage());
            throw new InternalServerErrorException(M("auth.permission.jwt.issue"));
        }

        request.setAttribute(AuthConfig.AUTH_ATTR, payload);

        return true;
    }
}

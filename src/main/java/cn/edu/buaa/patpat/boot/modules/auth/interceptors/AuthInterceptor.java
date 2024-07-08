package cn.edu.buaa.patpat.boot.modules.auth.interceptors;

import cn.edu.buaa.patpat.boot.exceptions.UnauthorizedException;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtIssueException;
import cn.edu.buaa.patpat.boot.extensions.jwt.JwtVerifyException;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.auth.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;
    private final ICookieSetter jwtCookieSetter;
    private final ICookieSetter refreshCookieSetter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwt = jwtCookieSetter.get(request);
        if (jwt == null) {
            throw new UnauthorizedException("Missing JWT token");
        }

        AuthPayload payload;

        // Validate JWT first.
        try {
            authService.verifyJwt(jwt);
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
            throw new UnauthorizedException("JWT tokens expired");
        } catch (JwtIssueException e) {
            throw new UnauthorizedException("Failed to issue new JWT tokens");
        }

        return true;
    }
}

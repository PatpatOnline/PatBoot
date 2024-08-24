package cn.edu.buaa.patpat.boot.modules.auth.aspect;


import cn.edu.buaa.patpat.boot.common.utils.Requests;
import cn.edu.buaa.patpat.boot.exceptions.UnauthorizedException;
import cn.edu.buaa.patpat.boot.modules.auth.api.AuthApi;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

/**
 * It injects {@link AuthPayload} object into the method arguments.
 */
@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ValidatePermissionAspect {
    private final AuthApi authApi;

    @Around("@annotation(cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission)")
    @Order(200)
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        ValidatePermission permission = method.getAnnotation(ValidatePermission.class);

        AuthPayload payload = validatePermission(method, permission.value());
        if (payload != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof AuthPayload) {
                    args[i] = payload;
                    break;
                }
            }
        }

        return point.proceed(args);
    }

    private AuthPayload validatePermission(Method method, AuthLevel level) {
        if (level == AuthLevel.NONE) {
            return null;
        }
        HttpServletRequest request = Requests.getCurrentRequest();
        AuthPayload auth = authApi.getAuth(request);
        if (auth == null) {
            log.error("Require authentication for method {}", method.getName());
            throw new UnauthorizedException(M("auth.login.not"));
        } else if ((level == AuthLevel.TEACHER) && !auth.isTeacher()) {
            log.error("Require teacher permission for method {}", method.getName());
            throw new UnauthorizedException(M("auth.permission.denied"));
        } else if ((level == AuthLevel.TA) && !auth.isTa()) {
            log.error("Require T.A. permission for method {}", method.getName());
            throw new UnauthorizedException(M("auth.permission.denied"));
        }
        return auth;
    }
}

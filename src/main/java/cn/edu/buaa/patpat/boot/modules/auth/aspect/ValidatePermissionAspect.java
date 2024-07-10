package cn.edu.buaa.patpat.boot.modules.auth.aspect;


import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
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
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ValidatePermissionAspect {
    private final AuthApi authApi;

    @Around("@annotation(cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermission)")
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        ValidatePermission permission = method.getAnnotation(ValidatePermission.class);

        AuthPayload payload = validatePermission(method, args, permission.value());
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

    private AuthPayload validatePermission(Method method, Object[] args, AuthLevel level) {
        if (level == AuthLevel.NONE) {
            return null;
        }
        HttpServletRequest request = getRequest(method, args);
        AuthPayload auth = authApi.getAuth(request);
        if (auth == null) {
            log.error("Require authentication for method {}", method.getName());
            throw new UnauthorizedException("Require authentication");
        } else if ((level == AuthLevel.TEACHER) && !auth.isTeacher()) {
            log.error("Require teacher permission for method {}", method.getName());
            throw new UnauthorizedException("Require teacher permission");
        } else if ((level == AuthLevel.TA) && !auth.isTa()) {
            log.error("Require T.A. permission for method {}", method.getName());
            throw new UnauthorizedException("Require T.A. permission");
        }
        return auth;
    }

    private HttpServletRequest getRequest(Method method, Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        log.error("Require HttpServletRequest in method {}", method.getName());
        throw new InternalServerErrorException("Require HttpServletRequest in method");
    }
}

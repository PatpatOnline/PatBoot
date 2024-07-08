package cn.edu.buaa.patpat.boot.aspect;

import cn.edu.buaa.patpat.boot.annotations.RequestValidation;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.exceptions.UnauthorizedException;
import cn.edu.buaa.patpat.boot.modules.account.dto.AuthLevel;
import cn.edu.buaa.patpat.boot.modules.auth.api.AuthApi;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class RequestValidationAspect {
    private final AuthApi authApi;

    @Before("@annotation(cn.edu.buaa.patpat.boot.annotations.RequestValidation)")
    public void intercept(JoinPoint point) {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        RequestValidation validation = method.getAnnotation(RequestValidation.class);

        if (validation.checkParams()) {
            validateParams(method, args);
        }

        validatePermission(method, args, validation.authLevel());
    }

    private void validateParams(Method method, Object[] args) {
        for (Object arg : args) {
            if (arg == null) {
                log.error("Argument null in method {}", method.getName());
                throw new BadRequestException("Argument is null");
            }
            if (arg instanceof BindingResult result) {
                if (result.hasErrors()) {
                    log.error("Argument has errors in method {}", method.getName());
                    throw new BadRequestException("Argument has errors");
                }
            }
        }
    }

    private void validatePermission(Method method, Object[] args, AuthLevel level) {
        if (level == AuthLevel.NONE) {
            return;
        }
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest request) {
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
                return;
            }
        }
        log.error("Require HttpServletRequest in method {}", method.getName());
        throw new InternalServerErrorException("Require HttpServletRequest in method");
    }
}

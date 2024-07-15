package cn.edu.buaa.patpat.boot.aspect;

import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Method;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ValidateParametersAspect {
    @Before("@annotation(cn.edu.buaa.patpat.boot.aspect.ValidateParameters)")
    public void intercept(final JoinPoint point) {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();

        for (Object arg : args) {
            if (arg instanceof BindingResult result) {
                if (result.hasErrors()) {
                    log.error("Argument has errors in method {}", method.getName());
                    throw new BadRequestException(M("validation.params.error"));
                }
            }
        }
    }
}

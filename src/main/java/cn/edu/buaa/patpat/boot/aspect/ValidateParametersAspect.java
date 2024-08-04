package cn.edu.buaa.patpat.boot.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ValidateParametersAspect {
    @Before("@annotation(cn.edu.buaa.patpat.boot.aspect.ValidateParameters)")
    public void intercept(final JoinPoint point) {
        Object[] args = point.getArgs();
        for (Object arg : args) {
            if (arg instanceof IRequireValidation validation) {
                validation.validate();
            }
        }
    }
}

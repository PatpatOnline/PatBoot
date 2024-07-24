package cn.edu.buaa.patpat.boot.aspect;

import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@Aspect
@RequiredArgsConstructor
public class ValidatePaginationAspect {
    @Before("@annotation(cn.edu.buaa.patpat.boot.aspect.ValidatePagination)")
    public void intercept(final JoinPoint point) {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        ValidatePagination rule = method.getAnnotation(ValidatePagination.class);
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameters()[i].isAnnotationPresent(Page.class)) {
                validatePage((int) args[i], rule);
            } else if (method.getParameters()[i].isAnnotationPresent(PageSize.class)) {
                validatePageSize((int) args[i], rule);
            }
        }
    }

    private void validatePage(int page, ValidatePagination rule) {
        if (page < rule.basePage() || page > rule.maxPage()) {
            throw new BadRequestException(M("validation.pagination.page.invalid"));
        }
    }

    private void validatePageSize(int pageSize, ValidatePagination rule) {
        if (pageSize < rule.minPageSize() || pageSize > rule.maxPageSize()) {
            throw new BadRequestException(M("validation.pagination.size.invalid"));
        }
    }
}

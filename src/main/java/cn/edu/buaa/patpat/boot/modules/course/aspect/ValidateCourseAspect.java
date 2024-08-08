package cn.edu.buaa.patpat.boot.modules.course.aspect;


import cn.edu.buaa.patpat.boot.common.utils.Mappers;
import cn.edu.buaa.patpat.boot.common.utils.Requests;
import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ValidateCourseAspect {
    private final ICookieSetter courseCookieSetter;
    private final Mappers mappers;

    @Around("@annotation(cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse)")
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        HttpServletRequest request = Requests.getCurrentRequest();
        ValidateCourse annotation = method.getAnnotation(ValidateCourse.class);

        String cookies = courseCookieSetter.get(request);
        if (Strings.isNullOrBlank(cookies)) {
            throw new ForbiddenException(M("validation.course.select.not"));
        }
        cookies = Strings.fromBase64(cookies);

        CoursePayload payload;
        try {
            payload = mappers.fromJson(cookies, CoursePayload.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(M("course.cookies.invalid"));
        }

        if (!annotation.allowRoot() && payload.isRoot()) {
            throw new ForbiddenException(M("validation.course.root.not"));
        }

        // Find CoursePayload or course id annotated with @CourseId.
        // These two won't appear in the same method.
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameters()[i].isAnnotationPresent(CourseId.class)) {
                args[i] = payload.getCourseId();
                break;
            } else if (args[i] instanceof CoursePayload) {
                args[i] = payload;
                break;
            }
        }

        return point.proceed(args);
    }
}

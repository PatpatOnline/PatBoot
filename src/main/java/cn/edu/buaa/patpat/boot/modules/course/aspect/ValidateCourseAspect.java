package cn.edu.buaa.patpat.boot.modules.course.aspect;


import cn.edu.buaa.patpat.boot.common.utils.Requests;
import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
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

    @Around("@annotation(cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourse)")
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();

        // get HttpServletRequest in args
        HttpServletRequest request = Requests.getCurrentRequest();
        String course = courseCookieSetter.get(request);
        if (Strings.isNullOrEmpty(course)) {
            throw new ForbiddenException(M("validation.course.select.not"));
        }

        int courseId;
        try {
            courseId = Integer.parseInt(course);
        } catch (NumberFormatException e) {
            throw new BadRequestException(M("validation.course.id.invalid"));
        }

        // find courseId annotated with @CourseId
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameters()[i].isAnnotationPresent(CourseId.class)) {
                args[i] = courseId;
                break;
            }
        }

        return point.proceed(args);
    }
}

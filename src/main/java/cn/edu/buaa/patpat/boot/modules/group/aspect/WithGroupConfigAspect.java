/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.aspect;

import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupConfig;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupConfigService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

/**
 * This aspect requires {@link cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourseAspect} to be present.
 */
@Component
@Aspect
@RequiredArgsConstructor
public class WithGroupConfigAspect {
    private final GroupConfigService groupConfigService;

    @Around("@annotation(cn.edu.buaa.patpat.boot.modules.group.aspect.WithGroupConfig)")
    @Order(400)
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        WithGroupConfig annotation = method.getAnnotation(WithGroupConfig.class);

        int courseId = getCourseId(method, args);
        if (courseId == 0) {
            throw new BadRequestException(M("validation.group.config.missing.course"));
        }
        GroupConfig config = groupConfigService.get(courseId);
        if (annotation.requireEnabled() && !config.isEnabled()) {
            throw new ForbiddenException(M("validation.group.config.disabled"));
        }
        setGroupConfig(args, config);

        return point.proceed(args);
    }

    private int getCourseId(Method method, Object[] args) {
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (method.getParameters()[i].isAnnotationPresent(CourseId.class)) {
                return (int) args[i];
            } else if (args[i] instanceof CoursePayload) {
                return ((CoursePayload) args[i]).getCourseId();
            }
        }
        return 0;
    }

    private void setGroupConfig(Object[] args, GroupConfig config) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof GroupConfig) {
                args[i] = config;
                break;
            }
        }
    }
}

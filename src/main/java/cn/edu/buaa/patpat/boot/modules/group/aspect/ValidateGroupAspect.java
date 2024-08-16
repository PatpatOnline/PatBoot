package cn.edu.buaa.patpat.boot.modules.group.aspect;

import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.aspect.CourseId;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.Group;
import cn.edu.buaa.patpat.boot.modules.group.models.entities.GroupMember;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupConfigService;
import cn.edu.buaa.patpat.boot.modules.group.services.GroupService;
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
 * This aspect requires the following aspects:
 * <ul>
 * <li>{@link cn.edu.buaa.patpat.boot.modules.course.aspect.ValidateCourseAspect}
 * <li>{@link cn.edu.buaa.patpat.boot.modules.auth.aspect.ValidatePermissionAspect}
 */
@Component
@Aspect
@RequiredArgsConstructor
public class ValidateGroupAspect {
    private final GroupConfigService groupConfigService;
    private final GroupService groupService;

    @Around("@annotation(cn.edu.buaa.patpat.boot.modules.group.aspect.ValidateGroup)")
    @Order(401)
    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        ValidateGroup annotation = method.getAnnotation(ValidateGroup.class);

        int courseId = getCourseId(method, args);
        if (courseId == 0) {
            throw new BadRequestException(M("validation.group.config.missing.course"));
        }

        int accountId = getAccountId(method, args);
        if (accountId == 0) {
            throw new BadRequestException(M("validation.group.config.missing.account"));
        }

        GroupMember member = groupService.findMember(courseId, accountId);
        if (annotation.requireInGroup()) {
            if (member == null) {
                throw new ForbiddenException(M("validation.group.require.in.not"));
            }
            if (annotation.requireOwner() && !member.isOwner()) {
                throw new ForbiddenException(M("validation.group.require.owner"));
            }
        } else if (annotation.requireNotInGroup()) {
            if (member != null) {
                throw new ForbiddenException(M("validation.group.require.in"));
            }
        }
        setGroup(method, args, member);

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

    private int getAccountId(Method method, Object[] args) {
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (args[i] instanceof AuthPayload) {
                return ((AuthPayload) args[i]).getId();
            }
        }
        return 0;
    }

    private void setGroup(Method method, Object[] args, GroupMember member) {
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (args[i] instanceof Group) {
                if (member != null) {
                    args[i] = groupService.getGroup(member.getGroupId());
                } else {
                    args[i] = null;
                }
            } else if (args[i] instanceof GroupMember) {
                args[i] = member;
            }
        }
    }
}

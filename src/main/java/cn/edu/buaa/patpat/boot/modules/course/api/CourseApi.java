/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.api;

import cn.edu.buaa.patpat.boot.common.utils.Strings;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.extensions.mappers.Mappers;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.services.CourseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseApi {
    private final CourseService courseService;
    private final ICookieSetter courseCookieSetter;
    private final Mappers mappers;

    public List<Course> getAllAvailableCourses(AuthPayload auth) {
        return courseService.getAll(auth);
    }

    public Cookie setCourseCookie(int courseId, AuthPayload auth) {
        CoursePayload payload = courseService.getCoursePayload(courseId, auth);
        try {
            return courseCookieSetter.set(Strings.toBase64(mappers.toJson(payload)));
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("course.cookies.error");
        }
    }

    public Cookie cleanCourseCookie() {
        return courseCookieSetter.clean();
    }

    public String getCourseName(int courseId) {
        return courseService.getName(courseId);
    }
}

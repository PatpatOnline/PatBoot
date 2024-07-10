package cn.edu.buaa.patpat.boot.modules.course.api;

import cn.edu.buaa.patpat.boot.extensions.cookies.ICookieSetter;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.services.CourseService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CourseApi {
    private final CourseService courseService;
    private final ICookieSetter courseCookieSetter;

    public List<Course> getAllAvailableCourses(AuthPayload auth) {
        return courseService.getAll(auth);
    }

    public Cookie cleanCourseCookie() {
        return courseCookieSetter.clean();
    }
}

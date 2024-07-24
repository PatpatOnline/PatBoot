package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.CourseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService extends BaseService {
    private final CourseMapper courseMapper;

    public Course create(CreateCourseRequest request) {
        Course course = mappers.map(request, Course.class);
        course.setActive(true);
        courseMapper.save(course);
        return course;
    }

    public Course delete(int id) {
        Course course = courseMapper.find(id);
        if (course != null) {
            courseMapper.delete(course.getId());
        }
        return course;
    }

    public Course update(int id, UpdateCourseRequest request) {
        Course course = courseMapper.find(id);
        if (course == null) {
            return null;
        }
        mappers.map(request, course);
        courseMapper.update(course);
        return course;
    }

    public List<Course> getAll(AuthPayload auth) {
        if (auth.isTa()) {
            return courseMapper.getAll();
        }
        return courseMapper.findAllActive(auth.getId());
    }

    public Course findById(int id) {
        return courseMapper.find(id);
    }

    public Course tryGetCourse(AuthPayload auth, int courseId) {
        if (auth.isTa()) {
            return courseMapper.find(courseId);
        }
        return courseMapper.findActive(courseId, auth.getId());
    }
}

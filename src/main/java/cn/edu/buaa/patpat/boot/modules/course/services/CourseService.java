package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CreateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateCourseRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Course;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.CourseTutorial;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Student;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.CourseMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.views.CourseView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService extends BaseService {
    private final CourseMapper courseMapper;
    private final StudentService studentService;

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

    public CourseView find(int id) {
        return courseMapper.findView(id);
    }

    public Course find(AuthPayload auth, int courseId) {
        if (auth.isTa()) {
            return courseMapper.find(courseId);
        }
        return courseMapper.findActive(courseId, auth.getId());
    }

    public String getName(int courseId) {
        Course course = courseMapper.find(courseId);
        if (course == null) {
            throw new NotFoundException(M("course.exists.not"));
        }
        return course.getName();
    }

    public CourseTutorial updateTutorial(int courseId, String url) {
        CourseTutorial tutorial = new CourseTutorial(courseId, url);
        courseMapper.updateTutorial(tutorial);
        return getTutorial(courseId);
    }

    public void deleteTutorial(int courseId) {
        int updated = courseMapper.deleteTutorial(courseId);
        if (updated == 0) {
            throw new NotFoundException(M("course.tutorial.exists.not"));
        }
    }

    public CourseTutorial getTutorial(int courseId) {
        var tutorial = courseMapper.findTutorial(courseId);
        if (tutorial == null) {
            throw new NotFoundException(M("course.tutorial.exists.not"));
        }
        return tutorial;
    }

    public CoursePayload getCoursePayload(int courseId, AuthPayload auth) {
        CoursePayload payload = new CoursePayload();

        Course course = find(auth, courseId);
        if (course == null) {
            throw new NotFoundException(M("course.exists.not"));
        }
        payload.setCourseId(course.getId());

        Student student = studentService.find(auth.getId(), course.getId());
        if (student != null) {
            payload.setStudentId(student.getId());
            payload.setTeacherId(student.getTeacherId());
        }

        return payload;
    }
}

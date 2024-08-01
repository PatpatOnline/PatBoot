package cn.edu.buaa.patpat.boot.modules.course.services;

import cn.edu.buaa.patpat.boot.common.dto.PageListDto;
import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.account.models.entities.Account;
import cn.edu.buaa.patpat.boot.modules.account.models.mappers.AccountFilterMapper;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.course.dto.UpdateStudentRequest;
import cn.edu.buaa.patpat.boot.modules.course.models.entities.Student;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentFilter;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentFilterMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.mappers.StudentMapper;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentDetailView;
import cn.edu.buaa.patpat.boot.modules.course.models.views.StudentListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService extends BaseService {
    private final StudentFilterMapper studentFilterMapper;
    private final BucketApi bucketApi;
    private final StudentMapper studentMapper;
    private final AccountFilterMapper accountFilterMapper;

    /**
     * Student calls this API to get their own details.
     */
    public StudentDetailView getDetail(int accountId, int courseId) {
        var view = studentFilterMapper.findDetailViewByAccountAndCourse(accountId, courseId);
        if (view == null) {
            throw new NotFoundException(M("student.exists.not"));
        }
        view.setAvatar(bucketApi.recordToUrl(view.getAvatar()));
        return view;
    }

    /**
     * Admin calls this API to get the details of any student.
     */
    public StudentDetailView getDetail(int id) {
        var view = studentFilterMapper.findDetailViewById(id);
        if (view == null) {
            throw new NotFoundException(M("student.exists.not"));
        }
        view.setAvatar(bucketApi.recordToUrl(view.getAvatar()));
        return view;
    }

    public PageListDto<StudentListView> query(int courseId, int page, int pageSize, StudentFilter filter) {
        var count = studentFilterMapper.count(courseId, filter);
        List<StudentListView> students = count == 0
                ? List.of()
                : studentFilterMapper.query(courseId, page, pageSize, filter);
        return PageListDto.of(students, count, page, pageSize);
    }

    public StudentListView query(int id) {
        var student = studentFilterMapper.queryById(id);
        if (student == null) {
            throw new NotFoundException(M("student.exists.not"));
        }
        return student;
    }

    public Student update(int id, UpdateStudentRequest request) {
        var student = studentFilterMapper.findById(id);
        if (student == null) {
            throw new NotFoundException(M("student.exists.not"));
        }

        if (request.getTeacherId() != null) {
            if (request.getTeacherId() == 1) {
                throw new BadRequestException(M("student.teacher.not"));
            }
            Account role = accountFilterMapper.findRole(request.getTeacherId());
            if (!role.isTeacher()) {
                throw new BadRequestException(M("student.teacher.not"));
            }
        }
        mappers.map(request, student);
        studentMapper.update(student);

        return student;
    }
}

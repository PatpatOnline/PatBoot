package cn.edu.buaa.patpat.boot.modules.task.services;

import cn.edu.buaa.patpat.boot.config.Globals;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.task.dto.SubmitLabRequest;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskScore;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabService extends TaskSubmissionService {
    private final TaskScoreMapper taskScoreMapper;

    public TaskScore submit(SubmitLabRequest request) {
        AuthPayload auth = request.getAuth();
        CoursePayload course = request.getCourse();

        TaskStatus status = checkSubmissionStatus(request.getLabId(), course.getCourseId(), TaskTypes.LAB, auth);
        String record = saveSubmission(request.getLabId(), auth, Globals.LAB_TAG, request.getFile());

        TaskScore score = new TaskScore(
                request.getLabId(),
                course.getCourseId(),
                auth.getId(),
                course.getStudentId(),
                TaskScore.NOT_GRADED,
                status == TaskStatus.OVERDUE,
                record);
        taskScoreMapper.saveOrUpdate(score);

        return taskScoreMapper.find(score.getTaskId(), score.getCourseId(), score.getAccountId());
    }
}

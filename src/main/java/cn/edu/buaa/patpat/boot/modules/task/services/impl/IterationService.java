/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.services.impl;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.judge.api.JudgeApi;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmissionDto;
import cn.edu.buaa.patpat.boot.modules.judge.dto.SubmitRequest;
import cn.edu.buaa.patpat.boot.modules.problem.api.ProblemApi;
import cn.edu.buaa.patpat.boot.modules.problem.dto.ProblemDto;
import cn.edu.buaa.patpat.boot.modules.task.dto.SubmitTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskProblem;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskScore;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskProblemMapper;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskStatus;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class IterationService extends TaskSubmissionService {
    private final TaskProblemMapper taskProblemMapper;
    private final JudgeApi judgeApi;
    private final ProblemApi problemApi;

    public SubmissionDto submit(SubmitTaskRequest request, String language) {
        AuthPayload auth = request.getAuth();
        CoursePayload course = request.getCourse();

        TaskStatus status = checkSubmissionStatus(request.getId(), course.getCourseId(), TaskTypes.ITERATION, auth);
        List<TaskProblem> problems = taskProblemMapper.find(request.getId());
        if (problems.isEmpty()) {
            throw new ForbiddenException(M("task.submit.iter.empty"));
        }
        int problemId = problems.get(0).getProblemId();

        String record = saveSubmission(request.getId(), auth, Globals.ITERATION_TAG, request.getFile());
        TaskScore score = new TaskScore(
                request.getId(),
                course.getCourseId(),
                auth.getId(),
                course.getStudentId(),
                Globals.NOT_GRADED,
                status == TaskStatus.OVERDUE,
                record);
        taskScoreMapper.saveOrUpdate(score);

        String filePath = bucketApi.recordToPrivatePath(record);
        var submitRequest = new SubmitRequest(auth.getId(), auth.getBuaaId(), course.getCourseId(), problemId, language, filePath);
        // enable auto grading
        submitRequest.setTaskId(request.getId());

        return judgeApi.submit(submitRequest);
    }

    public ProblemDto getIterationProblem(int taskId, int courseId, AuthPayload auth) {
        checkSubmissionStatus(taskId, courseId, TaskTypes.ITERATION, auth);

        List<TaskProblem> problems = taskProblemMapper.find(taskId);
        if (problems.isEmpty()) {
            throw new ForbiddenException(M("task.submit.forbidden", TaskTypes.toString(TaskTypes.ITERATION)));
        }
        int problemId = problems.get(0).getProblemId();

        return problemApi.query(problemId);
    }
}

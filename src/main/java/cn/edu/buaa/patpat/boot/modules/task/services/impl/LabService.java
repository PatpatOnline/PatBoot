/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.services.impl;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.course.dto.CoursePayload;
import cn.edu.buaa.patpat.boot.modules.task.dto.SubmitTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskScore;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskStatus;
import cn.edu.buaa.patpat.boot.modules.task.services.TaskSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabService extends TaskSubmissionService {
    public TaskScore submit(SubmitTaskRequest request) {
        AuthPayload auth = request.getAuth();
        CoursePayload course = request.getCourse();

        TaskStatus status = checkSubmissionStatus(request.getId(), course.getCourseId(), TaskTypes.LAB, auth);
        String record = saveSubmission(request.getId(), auth, Globals.LAB_TAG, request.getFile());

        TaskScore score = new TaskScore(
                request.getId(),
                course.getCourseId(),
                auth.getId(),
                course.getStudentId(),
                Globals.FULL_SCORE,
                status == TaskStatus.OVERDUE,
                record);
        taskScoreMapper.saveOrUpdate(score);

        return taskScoreMapper.find(score.getTaskId(), score.getCourseId(), score.getAccountId());
    }
}

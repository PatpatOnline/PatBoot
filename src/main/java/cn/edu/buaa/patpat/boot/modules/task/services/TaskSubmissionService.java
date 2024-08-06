package cn.edu.buaa.patpat.boot.modules.task.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.InternalServerErrorException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.auth.models.AuthPayload;
import cn.edu.buaa.patpat.boot.modules.bucket.api.BucketApi;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.Task;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

public abstract class TaskSubmissionService extends BaseService {
    @Autowired
    protected TaskMapper taskMapper;
    @Autowired
    protected BucketApi bucketApi;

    protected Task findTask(int taskId, int courseId, int type) {
        Task task = taskMapper.findSubmit(taskId, courseId, type);
        if (task == null) {
            throw new NotFoundException(M("task.exists.not", TaskTypes.toString(type)));
        }
        return task;
    }

    protected TaskStatus checkSubmissionStatus(int taskId, int courseId, int type, AuthPayload auth) {
        Task lab = findTask(taskId, courseId, type);
        TaskStatus status = validateStatus(lab);
        if (isEarlyOrLate(status, auth)) {
            throw new ForbiddenException(M("task.submit.forbidden", TaskTypes.toString(type)));
        }
        return status;
    }

    protected String saveSubmission(int taskId, AuthPayload auth, String tag, MultipartFile file) {
        String filename = file.getOriginalFilename();
        String record;
        if (auth.isTa()) {
            record = bucketApi.toRecord(
                    auth.getBuaaId(),
                    tag,
                    String.valueOf(taskId),
                    filename);
        } else {
            record = bucketApi.toRecord(
                    tag,
                    String.valueOf(taskId),
                    String.format("%s-%s", auth.getBuaaId(), auth.getName()),
                    filename);
        }
        String path = bucketApi.recordToPrivatePath(record);
        try {
            Medias.ensureEmptyParentPath(path);
            Medias.save(path, file);
        } catch (IOException e) {
            throw new InternalServerErrorException(M("system.error.io"));
        }
        return record;
    }

    private TaskStatus validateStatus(Task task) {
        var now = LocalDateTime.now();
        if (task.getStartTime().isAfter(now)) {
            return TaskStatus.EARLY;
        } else if (task.getDeadlineTime().isAfter(now)) {
            return TaskStatus.PUNCTUAL;
        } else if (task.getEndTime().isAfter(now)) {
            return TaskStatus.OVERDUE;
        } else {
            return TaskStatus.LATE;
        }
    }

    private boolean isEarlyOrLate(TaskStatus status, AuthPayload auth) {
        if (auth.isTa()) {
            return true;
        }
        return status == TaskStatus.PUNCTUAL || status == TaskStatus.OVERDUE;
    }
}

package cn.edu.buaa.patpat.boot.modules.task.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.task.dto.CreateTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.dto.UpdateTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.Task;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class TaskService extends BaseService {
    private final TaskMapper taskMapper;

    public Task create(int type, int courseId, CreateTaskRequest request) {
        Task task = mappers.map(request, Task.class);
        task.setCourseId(courseId);
        task.setType(type);
        taskMapper.save(task);
        return task;
    }

    public Task update(int id, int courseId, int type, UpdateTaskRequest request) {
        Task task = taskMapper.findUpdate(id, courseId, type);
        if (task == null) {
            throw new NotFoundException(M("task.exists.not"));
        }

        mappers.map(request, task);
        request.setStartTime(task.getStartTime());
        request.setDeadlineTime(task.getDeadlineTime());
        request.setEndTime(task.getEndTime());
        request.validate();
        taskMapper.update(task);

        return task;
    }

    public void delete(int id, int courseId, int type) {
        int updated = taskMapper.delete(id, courseId, type);
        if (updated == 0) {
            throw new NotFoundException(M("task.exists.not"));
        }
    }
}

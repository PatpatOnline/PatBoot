package cn.edu.buaa.patpat.boot.modules.task.services;

import cn.edu.buaa.patpat.boot.common.requets.BaseService;
import cn.edu.buaa.patpat.boot.exceptions.ForbiddenException;
import cn.edu.buaa.patpat.boot.exceptions.NotFoundException;
import cn.edu.buaa.patpat.boot.modules.task.dto.CreateTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.dto.UpdateTaskRequest;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.IHasTimeRange;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.Task;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskScore;
import cn.edu.buaa.patpat.boot.modules.task.models.entities.TaskTypes;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskMapper;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskProblemMapper;
import cn.edu.buaa.patpat.boot.modules.task.models.mappers.TaskScoreMapper;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskListView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskProblemListView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskProblemView;
import cn.edu.buaa.patpat.boot.modules.task.models.views.TaskView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

@Service
@RequiredArgsConstructor
public class TaskService extends BaseService {
    private final TaskMapper taskMapper;
    private final TaskProblemMapper taskProblemMapper;
    private final TaskScoreMapper taskScoreMapper;

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

    public List<TaskListView> query(int courseId, int type, boolean visibleOnly) {
        return taskMapper.query(courseId, type, visibleOnly);
    }

    public TaskView query(int id, int courseId, int type, boolean validateTime) {
        TaskView task = taskMapper.query(id, courseId, type);
        if (task == null) {
            throw new NotFoundException(M("task.exists.not", TaskTypes.toString(type)));
        }
        if (validateTime) {
            validateStartTime(type, task);
        }
        return task;
    }

    public boolean exists(int id, int courseId) {
        return taskMapper.exists(id, courseId);
    }

    public boolean exists(int id, int courseId, int type) {
        return taskMapper.existsByType(id, courseId, type);
    }

    public List<TaskProblemListView> updateProblems(int id, int courseId, int type, List<Integer> problemIds) {
        if (!exists(id, courseId)) {
            throw new NotFoundException(M("task.exists.not", TaskTypes.toString(type)));
        }

        taskProblemMapper.batchDelete(id);
        if (problemIds.isEmpty()) {
            return List.of();
        }

        if (problemIds.size() > 1) {
            if (taskProblemMapper.count(problemIds) != problemIds.size()) {
                throw new NotFoundException(M("problem.exists.not"));
            }
            taskProblemMapper.batchAdd(id, problemIds);
        } else {
            // For iteration task, only one problem is allowed, and
            // 0 stands for no problem.
            int problemId = problemIds.get(0);
            if (problemId > 0) {
                if (taskProblemMapper.count(List.of(problemId)) != 1) {
                    throw new NotFoundException(M("problem.exists.not"));
                }
                taskProblemMapper.add(id, problemId);
            } else {
                return List.of();
            }
        }

        return taskProblemMapper.findTaskProblemList(id);
    }

    public List<TaskProblemListView> getProblems(int id) {
        return taskProblemMapper.findTaskProblemList(id);
    }

    public List<TaskProblemView> getProblems(int id, int courseId, int type, int accountId, boolean validateTime) {
        TaskView task = taskMapper.query(id, courseId, type);
        if (task == null) {
            throw new NotFoundException(M("task.exists.not", TaskTypes.toString(type)));
        }
        if (validateTime) {
            validateStartTime(type, task);
        }
        var problems = taskProblemMapper.findTaskProblems(id, accountId);
        problems.forEach(TaskProblemView::eraseTimestampIfNotSubmitted);
        return problems;
    }

    public TaskScore getScore(int id, int courseId, int type, int accountId, boolean validateTime) {
        TaskView task = taskMapper.query(id, courseId, type);
        if (task == null) {
            throw new NotFoundException(M("task.exists.not", TaskTypes.toString(type)));
        }
        if (validateTime) {
            validateStartTime(type, task);
        }

        TaskScore score = taskScoreMapper.find(id, courseId, accountId);
        if (score == null) {
            throw new NotFoundException(M("task.score.exists.not"));
        }

        return score;
    }

    private void validateStartTime(int type, IHasTimeRange range) {
        var now = LocalDateTime.now();
        if (range.getStartTime().isAfter(now)) {
            throw new ForbiddenException(M("task.started.not", TaskTypes.toString(type)));
        }
    }
}

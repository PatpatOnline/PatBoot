package cn.edu.buaa.patpat.boot.modules.task.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Task extends HasCreatedAndUpdated implements IHasTimeRange {
    private int id;
    private int courseId;

    /**
     * Type of the task.
     *
     * @see TaskTypes
     */
    private int type;

    private String title;
    private String content;
    private boolean visible;

    private LocalDateTime startTime;
    private LocalDateTime deadlineTime;
    private LocalDateTime endTime;
}

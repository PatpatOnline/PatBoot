package cn.edu.buaa.patpat.boot.modules.task.models.entities;

import java.time.LocalDateTime;

public interface IHasTimeRange {
    LocalDateTime getStartTime();

    LocalDateTime getDeadlineTime();

    LocalDateTime getEndTime();
}

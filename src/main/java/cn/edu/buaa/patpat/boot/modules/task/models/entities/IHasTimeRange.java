/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.models.entities;

import java.time.LocalDateTime;

public interface IHasTimeRange {
    LocalDateTime getStartTime();

    LocalDateTime getDeadlineTime();

    LocalDateTime getEndTime();
}

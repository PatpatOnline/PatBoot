/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.task.models.views;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskProblemView extends HasCreatedAndUpdated implements Serializable {
    private int problemId;
    private String title;

    private int score = Globals.NOT_SUBMITTED;

    public void eraseTimestampIfNotSubmitted() {
        if (score < 0) {
            setCreatedAt(null);
            setUpdatedAt(null);
        }
    }
}

package cn.edu.buaa.patpat.boot.modules.task.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import cn.edu.buaa.patpat.boot.config.Globals;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskProblemView extends HasCreatedAndUpdated {
    private int problemId;
    private String title;

    private int score = Globals.NOT_SUBMITTED;
}

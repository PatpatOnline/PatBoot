package cn.edu.buaa.patpat.boot.modules.task.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import cn.edu.buaa.patpat.boot.config.Globals;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaskScore extends HasCreatedAndUpdated {
    private int taskId;
    private int courseId;
    private int accountId;

    /**
     * 0 means the score belongs to T.A.
     */
    private int studentId;

    /**
     * By default, the score is -1, which means not submitted.
     */
    private int score = Globals.NOT_SUBMITTED;
    private boolean late;

    /**
     * The record of the submission, indicating the physical location.
     */
    private String record;
}

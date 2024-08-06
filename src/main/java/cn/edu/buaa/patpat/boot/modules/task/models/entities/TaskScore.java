package cn.edu.buaa.patpat.boot.modules.task.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaskScore extends HasCreatedAndUpdated {
    public static final int NOT_SUBMITTED = -2;
    public static final int NOT_GRADED = -1;

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
    private int score = NOT_SUBMITTED;
    private boolean late;

    /**
     * The record of the submission, indicating the physical location.
     */
    private String record;
}

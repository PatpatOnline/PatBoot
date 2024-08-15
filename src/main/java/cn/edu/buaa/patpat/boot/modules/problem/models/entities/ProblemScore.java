package cn.edu.buaa.patpat.boot.modules.problem.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemScore extends HasCreatedAndUpdated {
    private int problemId;
    private int accountId;
    private int score;
}

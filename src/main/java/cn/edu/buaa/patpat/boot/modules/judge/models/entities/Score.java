package cn.edu.buaa.patpat.boot.modules.judge.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Score extends HasCreatedAndUpdated {
    private int problemId;
    private int accountId;
    private int score;

}

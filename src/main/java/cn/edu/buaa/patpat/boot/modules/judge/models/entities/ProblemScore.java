package cn.edu.buaa.patpat.boot.modules.judge.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProblemScore extends HasCreatedAndUpdated implements Serializable {
    private int problemId;
    private int accountId;
    private int score;
}

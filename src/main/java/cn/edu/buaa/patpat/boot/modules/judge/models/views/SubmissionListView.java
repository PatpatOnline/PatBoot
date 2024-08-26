package cn.edu.buaa.patpat.boot.modules.judge.models.views;

import cn.edu.buaa.patpat.boot.modules.judge.models.JudgeTimestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmissionListView extends JudgeTimestamp implements Serializable {
    private int id;
    private int accountId;

    private String buaaId;
    private String name;

    private int problemId;
    private String problemName;

    private String language;
    private int score;
}

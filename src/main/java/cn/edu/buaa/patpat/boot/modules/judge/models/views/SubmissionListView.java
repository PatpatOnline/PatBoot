package cn.edu.buaa.patpat.boot.modules.judge.models.views;

import cn.edu.buaa.patpat.boot.modules.judge.models.JudgeTimestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubmissionListView extends JudgeTimestamp {
    private int id;
    private int accountId;

    private String buaaId;
    private String name;

    private int problemId;
    private String problemName;

    private String language;
    private int score;
}

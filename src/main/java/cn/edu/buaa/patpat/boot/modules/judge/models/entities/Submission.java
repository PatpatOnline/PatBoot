package cn.edu.buaa.patpat.boot.modules.judge.models.entities;

import cn.edu.buaa.patpat.boot.modules.judge.models.JudgeTimestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Submission extends JudgeTimestamp {
    private int id;
    private int accountId;
    private String buaaId;
    private int courseId;
    private int problemId;

    private String language;
    private int score;
    private String data;
}

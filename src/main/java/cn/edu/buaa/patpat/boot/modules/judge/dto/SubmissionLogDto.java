package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SubmissionLogDto {
    private int id;
    private String buaaId;
    private int courseId;
    private int problemId;
}

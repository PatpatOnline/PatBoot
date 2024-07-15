package cn.edu.buaa.patpat.boot.modules.judge.dto;

import lombok.Data;

@Data
public class JudgePayload {
    private int accountId;
    private String buaaId;
    private int courseId;

    /**
     * Path of the sandbox directory under /judge/.
     * Although we can simply calculate it again, this way
     * we can get it easier after the submission is done.
     */
    private String sandboxPath;
}

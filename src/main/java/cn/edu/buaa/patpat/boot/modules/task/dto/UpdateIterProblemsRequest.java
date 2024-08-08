package cn.edu.buaa.patpat.boot.modules.task.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateIterProblemsRequest {
    /**
     * If list is empty, all problems will be removed.
     */
    @NotNull
    private int problemId;
}

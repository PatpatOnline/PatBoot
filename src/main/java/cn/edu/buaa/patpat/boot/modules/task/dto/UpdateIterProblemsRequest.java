package cn.edu.buaa.patpat.boot.modules.task.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateIterProblemsRequest {
    @NotNull
    private Integer problemId;
}

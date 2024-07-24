package cn.edu.buaa.patpat.boot.modules.discussion.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateReplyRequest {
    @Size(min = 1, max = 65535)
    @NotNull
    private String content;
}

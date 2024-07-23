package cn.edu.buaa.patpat.boot.modules.discussion.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateReplyRequest {
    @Min(1)
    private int discussionId;

    /**
     * 0 for root reply, otherwise the parent reply id.
     */
    @Min(0)
    private int parentId;

    @Size(min = 1, max = 65535)
    @NotNull
    private String content;
}

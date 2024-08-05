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
     * 0 for root reply, the id of the reply to which this reply is replying to.
     * (So many replies in one sentence, I know.)
     */
    @Min(0)
    private int toId;

    @Size(min = 1, max = 65535)
    @NotNull
    private String content;
}

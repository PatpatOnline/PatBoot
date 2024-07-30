package cn.edu.buaa.patpat.boot.modules.discussion.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReplyView extends HasCreatedAndUpdated {
    private int id;
    private int parentId;

    @JsonIgnore
    private int authorId;
    private DiscussionAccountView author;

    private String content;
    private boolean verified;

    private int likeCount;
    private boolean liked;
}

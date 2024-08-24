package cn.edu.buaa.patpat.boot.modules.discussion.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DiscussionView extends HasCreatedAndUpdated {
    private static final int SUMMARY_CONTENT_LENGTH = 50;

    private int id;
    private int type;

    @JsonIgnore
    private int courseId;
    @JsonIgnore
    private int authorId;

    private String title;
    private String content;

    private boolean topped;
    private boolean starred;

    private int likeCount;
    private boolean liked;
    private int replyCount;

    private boolean subscribed;

    private DiscussionAccountView author;
}

package cn.edu.buaa.patpat.boot.modules.discussion.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReplyView extends HasCreatedAndUpdated implements Serializable {
    private int id;
    @JsonIgnore
    private int parentId;
    @JsonIgnore
    private int toId;
    private String toName;

    @JsonIgnore
    private int authorId;
    private DiscussionAccountView author;

    private String content;
    private boolean verified;

    private int likeCount;
    private boolean liked;

    private List<ReplyView> replies;

    @JsonIgnore
    public String getAuthorName() {
        return author != null ? author.getName() : null;
    }
}

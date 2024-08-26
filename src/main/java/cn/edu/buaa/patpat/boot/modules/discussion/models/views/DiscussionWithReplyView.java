package cn.edu.buaa.patpat.boot.modules.discussion.models.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionWithReplyView implements Serializable {
    private DiscussionView discussion;
    private List<ReplyView> replies;
}

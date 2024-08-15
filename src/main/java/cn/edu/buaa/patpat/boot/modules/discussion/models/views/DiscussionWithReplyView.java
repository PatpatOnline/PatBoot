package cn.edu.buaa.patpat.boot.modules.discussion.models.views;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionWithReplyView {
    private DiscussionView discussion;
    private List<ReplyView> replies;
}

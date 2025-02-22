/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscussionNotification {
    private int discussionId;
    private int replyId;

    // who sent the reply
    private String buaaId;
    private String name;
}

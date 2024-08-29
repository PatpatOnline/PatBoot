/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.discussion.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Reply extends HasCreatedAndUpdated implements Serializable {
    private int id;
    private int discussionId;
    private int parentId;
    private int toId;

    private int authorId;
    private String content;

    private boolean verified;
}

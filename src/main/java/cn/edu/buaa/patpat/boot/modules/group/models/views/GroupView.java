/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.views;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GroupView implements Serializable {
    private int id;
    private String name;
    private String description;
    private boolean locked;
    private int maxSize;

    private List<GroupMemberView> members;

    public int getMemberCount() {
        return members.size();
    }
}

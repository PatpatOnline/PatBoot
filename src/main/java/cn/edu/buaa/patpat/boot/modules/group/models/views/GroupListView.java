package cn.edu.buaa.patpat.boot.modules.group.models.views;

import lombok.Data;

@Data
public class GroupListView {
    private int id;
    private String name;
    private String description;
    private boolean locked;

    private int maxSize;
    private int memberCount;

    private String ownerBuaaId;
    private String ownerName;
}

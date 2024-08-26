package cn.edu.buaa.patpat.boot.modules.group.models.views;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupListView implements Serializable {
    private int id;
    private String name;
    private String description;
    private boolean locked;

    private int maxSize;
    private int memberCount;

    private String ownerBuaaId;
    private String ownerName;
}

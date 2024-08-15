package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import lombok.Data;

@Data
public class Group {
    private int id;
    private int courseId;

    private String name;
    private String description;

    /**
     * If the group is locked, no one can join the group.
     */
    private boolean locked;
}

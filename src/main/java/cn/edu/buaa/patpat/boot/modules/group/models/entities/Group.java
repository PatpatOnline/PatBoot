package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class Group implements Serializable {
    private int id;
    private int courseId;

    private String name;
    private String description;

    /**
     * If the group is locked, no one can join the group.
     */
    private boolean locked;
}

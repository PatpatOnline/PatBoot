package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import lombok.Data;

@Data
public class GroupConfig {
    private int courseId;

    /**
     * Maximum number of members in the group.
     */
    private int maxSize;

    /**
     * Whether enable group feature.
     */
    private boolean enabled;
}

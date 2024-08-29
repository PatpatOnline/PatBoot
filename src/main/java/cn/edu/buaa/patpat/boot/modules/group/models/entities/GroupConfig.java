/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class GroupConfig implements Serializable {
    private int courseId;

    /**
     * Maximum number of members in the group.
     */
    private int maxSize;

    /**
     * Minimum weight of the group members.
     */
    private int minWeight;

    /**
     * Maximum weight of the group members.
     */
    private int maxWeight;

    /**
     * Whether enable group feature.
     */
    private boolean enabled;
}

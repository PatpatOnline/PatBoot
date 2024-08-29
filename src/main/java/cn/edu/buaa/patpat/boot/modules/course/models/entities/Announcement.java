/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Announcement extends HasCreatedAndUpdated implements Serializable {
    private int id;
    private int courseId;

    /**
     * Account ID of the author.
     */
    private int accountId;

    private String title;
    private String content;

    private boolean topped;
}

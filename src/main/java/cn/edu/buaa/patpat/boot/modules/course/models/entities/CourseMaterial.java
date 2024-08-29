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
public class CourseMaterial extends HasCreatedAndUpdated implements Serializable {
    private int id;
    private int courseId;

    /**
     * This should be unique in a course, check in the service layer.
     */
    private String filename;

    /**
     * Optional comment for the material, it can be null.
     */
    private String comment;
}

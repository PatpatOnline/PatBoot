/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.course.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CourseTutorial extends HasCreatedAndUpdated implements Serializable {
    /**
     * The course only has one tutorial, so the course_id is the primary key.
     */
    private int courseId;

    /**
     * This is the complete URL of the tutorial, including the protocol.
     * So that it can be directly used in the iframe.
     */
    private String url;
}

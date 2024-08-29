/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.problem.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Problem extends HasCreatedAndUpdated implements Serializable {
    private int id;

    private String title;
    private String description;

    /**
     * Submission of hidden problems can only be invoked within.
     * e.g., problem for iterative practice.
     */
    private boolean hidden;

    /**
     * JSON format of the problem descriptor.
     * See {@link cn.edu.buaa.patpat.boot.modules.problem.models.entities.ProblemDescriptor}.
     */
    private String data;
}

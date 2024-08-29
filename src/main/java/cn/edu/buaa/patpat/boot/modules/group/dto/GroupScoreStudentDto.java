/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.modules.group.dto;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Group score for students. Here we don't add the score field for privacy reasons.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupScoreStudentDto extends HasCreatedAndUpdated {
}

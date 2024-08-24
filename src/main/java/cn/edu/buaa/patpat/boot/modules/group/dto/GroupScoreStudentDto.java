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

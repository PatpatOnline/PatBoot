package cn.edu.buaa.patpat.boot.modules.course.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CourseTutorial extends HasCreatedAndUpdated {
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

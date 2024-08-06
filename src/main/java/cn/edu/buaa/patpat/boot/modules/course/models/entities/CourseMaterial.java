package cn.edu.buaa.patpat.boot.modules.course.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseMaterial extends HasCreatedAndUpdated {
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

package cn.edu.buaa.patpat.boot.modules.course.models.entities;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class Course extends HasCreatedAndUpdated implements Serializable {
    private int id;

    /**
     * The name of the course.
     */
    private String name;

    /**
     * The code of the course. e.g. BXXXXXXX
     */
    private String code;

    /**
     * The semester of the course. e.g., 2023 Autumn
     */
    private String semester;

    /**
     * Past courses will be marked as inactive.
     */
    private Boolean active;
}

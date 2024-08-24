package cn.edu.buaa.patpat.boot.modules.course.dto;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseTutorialDto extends HasCreatedAndUpdated {
    private String url;
}

package cn.edu.buaa.patpat.boot.modules.course.dto;

import cn.edu.buaa.patpat.boot.common.dto.HasTimestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseTutorialDto extends HasTimestamp {
    private String url;
}

package cn.edu.buaa.patpat.boot.modules.problem.dto;

import cn.edu.buaa.patpat.boot.common.dto.HasTimestamp;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.ProblemDescriptor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemDto extends HasTimestamp {
    private int id;
    private String title;
    private boolean hidden;
    private String description;
    private ProblemDescriptor descriptor;
}

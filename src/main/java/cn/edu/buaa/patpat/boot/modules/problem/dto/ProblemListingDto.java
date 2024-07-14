package cn.edu.buaa.patpat.boot.modules.problem.dto;

import cn.edu.buaa.patpat.boot.common.dto.HasTimestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemListingDto extends HasTimestamp {
    private int id;
    private String title;
    private boolean hidden;
}

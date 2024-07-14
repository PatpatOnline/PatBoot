package cn.edu.buaa.patpat.boot.modules.problem.dto;

import cn.edu.buaa.patpat.boot.common.dto.HasTimestamp;
import cn.edu.buaa.patpat.boot.common.utils.Mappers;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.ProblemDescriptor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateProblemResponse extends HasTimestamp {
    private int id;
    private String title;
    private boolean hidden;
    private ProblemDescriptor descriptor;

    public static CreateProblemResponse of(Problem problem, Mappers mappers) {
        CreateProblemResponse dto = mappers.map(problem, CreateProblemResponse.class);
        dto.setDescriptor(mappers.fromJson(problem.getData(), ProblemDescriptor.class, null));
        return dto;
    }
}

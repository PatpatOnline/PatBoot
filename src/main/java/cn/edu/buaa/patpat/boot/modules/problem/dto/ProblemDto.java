package cn.edu.buaa.patpat.boot.modules.problem.dto;

import cn.edu.buaa.patpat.boot.common.dto.HasTimestamp;
import cn.edu.buaa.patpat.boot.common.utils.Mappers;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.Problem;
import cn.edu.buaa.patpat.boot.modules.problem.models.entities.ProblemDescriptor;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    public static ProblemDto of(Problem problem, Mappers mappers) {
        ProblemDto dto = mappers.map(problem, ProblemDto.class);
        try {
            dto.setDescriptor(mappers.fromJson(problem.getData(), ProblemDescriptor.class));
        } catch (JsonProcessingException e) {
            // ignore
        }
        return dto;
    }
}

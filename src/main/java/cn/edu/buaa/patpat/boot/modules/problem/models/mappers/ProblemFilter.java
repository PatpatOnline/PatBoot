package cn.edu.buaa.patpat.boot.modules.problem.models.mappers;

import cn.edu.buaa.patpat.boot.common.utils.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProblemFilter {
    private String title;
    private Boolean hidden;

    public boolean isEmpty() {
        return Strings.isNullOrBlank(title) && hidden == null;
    }
}

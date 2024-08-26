package cn.edu.buaa.patpat.boot.modules.problem.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemListView extends HasCreatedAndUpdated implements Serializable {
    private int id;
    private String title;
    private boolean hidden;
}

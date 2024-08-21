package cn.edu.buaa.patpat.boot.modules.group.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupScoreListView extends HasCreatedAndUpdated {
    private int groupId;
    private int score;
}

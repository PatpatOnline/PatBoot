package cn.edu.buaa.patpat.boot.modules.group.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupScoreListView extends HasCreatedAndUpdated implements Serializable {
    private int groupId;
    private int score;
}

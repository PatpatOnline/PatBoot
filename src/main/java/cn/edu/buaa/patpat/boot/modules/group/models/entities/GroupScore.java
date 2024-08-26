package cn.edu.buaa.patpat.boot.modules.group.models.entities;

import cn.edu.buaa.patpat.boot.common.Globals;
import cn.edu.buaa.patpat.boot.common.models.HasCreatedAndUpdated;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupScore extends HasCreatedAndUpdated implements Serializable {
    private int groupId;
    private int courseId;
    private int score = Globals.NOT_SUBMITTED;

    private String record;
}

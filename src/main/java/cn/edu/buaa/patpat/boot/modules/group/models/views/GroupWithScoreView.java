package cn.edu.buaa.patpat.boot.modules.group.models.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class GroupWithScoreView implements Serializable {
    private GroupView group;
    private GroupScoreListStudentView submission;
}

package cn.edu.buaa.patpat.boot.modules.group.models.views;

import lombok.Data;

@Data
public class GroupScoreInfoView {
    private int groupId;
    private int courseId;
    private int score;
}

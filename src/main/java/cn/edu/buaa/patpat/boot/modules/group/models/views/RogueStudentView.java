package cn.edu.buaa.patpat.boot.modules.group.models.views;

import lombok.Data;

@Data
public class RogueStudentView {
    private int accountId;
    private String buaaId;
    private String name;

    private int teacherId;
}

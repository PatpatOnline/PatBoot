package cn.edu.buaa.patpat.boot.modules.course.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentListView extends HasCreated {
    private int id;
    private int accountId;
    private String buaaId;

    private String studentName;
    private int teacherId;

    private String school;
    private String major;
    private String className;
    private boolean repeat;
}

package cn.edu.buaa.patpat.boot.modules.course.models.views;

import cn.edu.buaa.patpat.boot.common.models.HasCreated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentView extends HasCreated {
    private int id;
    private int accountId;
    private String buaaId;

    private String studentName;
    private String teacherName;

    private String school;
}
